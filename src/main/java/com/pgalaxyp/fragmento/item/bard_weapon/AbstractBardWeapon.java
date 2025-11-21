package com.pgalaxyp.fragmento.item.bard_weapon;

import com.pgalaxyp.fragmento.entity.bard.angels.AbstractAngel;
import com.pgalaxyp.fragmento.util.BardComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.List;

public abstract class AbstractBardWeapon extends Item {

    private static final int MAX_CHARGES = 2;
    private static final int MAX_COMBO_HITS = 2;
    private static final int COOLDOWN_TICKS = 20;
    protected static final double SPECIAL_RANGE = 4.5D;
    private static final int PARTICLE_POINTS = 48;
    private static final double[] CIRCLE_COS = new double[PARTICLE_POINTS];
    private static final double[] CIRCLE_SIN = new double[PARTICLE_POINTS];

    static {
        double twoPi = Math.PI * 2.0D;
        for (int i = 0; i < PARTICLE_POINTS; i++) {
            double angle = twoPi * i / PARTICLE_POINTS;
            CIRCLE_COS[i] = Math.cos(angle);
            CIRCLE_SIN[i] = Math.sin(angle);
        }
    }

    public AbstractBardWeapon(Properties properties) {
        super(properties);
    }

    public static void handleAction(ServerPlayer player, ItemStack stack, boolean special, boolean ultimate) {
        Item item = stack.getItem();
        if (!(item instanceof AbstractBardWeapon weapon)) return;

        if (special) {
            weapon.handleSpecial(player, stack);
            return;
        }

        if (ultimate) {
            weapon.handleUltimate(player, stack);
            return;
        }

        weapon.handleNormal(player, stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel && player instanceof ServerPlayer serverPlayer) {
            handleAction(serverPlayer, stack, true, false);
        }
        spawnSpecialParticlesClient(level, player);
        return InteractionResultHolder.pass(stack);
    }

    protected void handleNormal(ServerPlayer player, ItemStack stack) {
        var cooldowns = player.getCooldowns();
        if (cooldowns.isOnCooldown(this)) return;

        int comboIndex = getComboIndex(stack);
        int nextComboIndex = comboIndex + 1;
        if (nextComboIndex >= MAX_COMBO_HITS) {
            nextComboIndex = 0;
        }

        boolean charged = nextComboIndex == 0;
        setComboIndex(stack, nextComboIndex);

        cooldowns.addCooldown(this, COOLDOWN_TICKS);

        Level level = player.level();
        if (!(level instanceof ServerLevel server)) return;

        Projectile projectile = buildProjectile(server, player, stack, charged);
        if (projectile != null) {
            server.addFreshEntity(projectile);
        }
    }

    protected void handleSpecial(ServerPlayer player, ItemStack stack) {
        Level level = player.level();
        if (!(level instanceof ServerLevel server)) return;
        specialAbility(server, player, stack);
    }

    protected void handleUltimate(ServerPlayer player, ItemStack stack) {
        if (!hasMaxCharges(stack)) return;

        var cooldowns = player.getCooldowns();
        if (cooldowns.isOnCooldown(this)) return;

        ultimateAbility(player, stack);
    }

    protected abstract Projectile createProjectile(ServerLevel server, ServerPlayer player, ItemStack stack, boolean charged);

    protected final Projectile buildProjectile(ServerLevel server, ServerPlayer player, ItemStack stack, boolean charged) {
        if (charged) {
            incrementCharge(stack);
        }
        Projectile projectile = createProjectile(server, player, stack, charged);
        if (projectile != null) {
            projectile.setOwner(player);
            projectile.setPos(player.getEyePosition());
        }
        return projectile;
    }

    protected abstract void specialAbility(ServerLevel level, ServerPlayer player, ItemStack stack);

    protected abstract EntityType<? extends AbstractAngel> getAngelType();

    protected AABB getSpecialRangeAABB(Player player) {
        return player.getBoundingBox().inflate(SPECIAL_RANGE);
    }

    protected void ultimateAbility(ServerPlayer player, ItemStack stack) {
        spawnAngel(player);
        resetCharges(stack);
    }

    private void spawnAngel(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        EntityType<? extends AbstractAngel> angelType = getAngelType();
        if (angelType == null) return;

        AbstractAngel angel = angelType.create(level, null, player.blockPosition(), MobSpawnType.TRIGGERED, false, false);
        if (angel == null) return;

        angel.setOwner(player);
        angel.setPos(player.getX(), player.getY() + 2, player.getZ());
        level.addFreshEntity(angel);
    }

    protected void spawnSpecialParticlesClient(Level level, Player player) {
        if (!level.isClientSide()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != player) return;

        Vector3f color = getSpecialColor();
        float size = 1.0F;
        DustParticleOptions particle = new DustParticleOptions(color, size);

        double cx = player.getX();
        double cz = player.getZ();
        double y = player.getY() + 0.1D;

        for (int i = 0; i < PARTICLE_POINTS; i++) {
            double px = cx + SPECIAL_RANGE * CIRCLE_COS[i];
            double pz = cz + SPECIAL_RANGE * CIRCLE_SIN[i];
            level.addParticle(particle, px, y, pz, 0.0D, 0.0D, 0.0D);
        }
    }

    protected abstract Vector3f getSpecialColor();

    protected <T extends Entity> List<T> getEntitiesInCircularRange(ServerLevel level, Player player, Class<T> type) {
        double r = SPECIAL_RANGE;
        double r2 = r * r;
        AABB box = player.getBoundingBox().inflate(r, r, r);
        double px = player.getX();
        double pz = player.getZ();

        return level.getEntitiesOfClass(type, box, e -> {
            Vec3 pos = e.position();
            double dx = pos.x - px;
            double dz = pos.z - pz;
            return dx * dx + dz * dz <= r2;
        });
    }

    protected int getChargedCount(ItemStack stack) {
        return stack.getOrDefault(BardComponents.CHARGED_ATTACKS_FIRED, 0);
    }

    protected void incrementCharge(ItemStack stack) {
        int current = getChargedCount(stack);
        if (current < MAX_CHARGES) {
            stack.set(BardComponents.CHARGED_ATTACKS_FIRED, current + 1);
        }
    }

    protected void resetCharges(ItemStack stack) {
        stack.set(BardComponents.CHARGED_ATTACKS_FIRED, 0);
    }

    protected boolean hasMaxCharges(ItemStack stack) {
        return getChargedCount(stack) >= MAX_CHARGES;
    }

    protected int getComboIndex(ItemStack stack) {
        return stack.getOrDefault(BardComponents.NORMAL_COMBO_INDEX, 0);
    }

    protected void setComboIndex(ItemStack stack, int index) {
        stack.set(BardComponents.NORMAL_COMBO_INDEX, index);
    }
}