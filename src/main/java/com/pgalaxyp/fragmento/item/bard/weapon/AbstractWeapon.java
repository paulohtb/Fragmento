package com.pgalaxyp.fragmento.item.bard.weapon;

import com.pgalaxyp.fragmento.entity.bard.angel.AbstractAngel;
import com.pgalaxyp.fragmento.util.BardComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

public abstract class AbstractWeapon extends Item {

    private static final int MAX_CHARGES = 2;
    private static final int MAX_COMBO_HITS = 2;
    private static final int COOLDOWN_TICKS = 20;
    private static final int DEFAULT_DEBUFF_COOLDOWN = 10;
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

    public AbstractWeapon(Properties properties) {
        super(properties);
    }

    public static void handleAction(ServerPlayer player, ItemStack stack, boolean isUseKeyDown) {
        Item item = stack.getItem();
        if (!(item instanceof AbstractWeapon weapon)) return;
        if (isUseKeyDown) {
            if (weapon.hasMaxCharges(stack)) weapon.performUltimateAbility(player, stack);
            return;
        }
        weapon.performNormalAbility(player, stack);
    }

    public static void handleHoldAction(ServerPlayer player, ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof AbstractWeapon weapon)) return;
        weapon.performSpecialAbility(player, stack);
    }

    protected int getMaxComboHits() {
        return MAX_COMBO_HITS;
    }

    protected int getCooldownTicks() {
        return COOLDOWN_TICKS;
    }

    protected boolean itemIsOnCooldown(ServerPlayer player) {
        return player.getCooldowns().isOnCooldown(this);
    }

    protected ServerLevel getServerLevel(Level level) {
        if (level instanceof ServerLevel server) return server;
        return null;
    }

    protected int computeNextComboIndex(ItemStack stack) {
        int comboIndex = getComboIndex(stack);
        int nextComboIndex = comboIndex + 1;
        if (nextComboIndex >= getMaxComboHits()) return 0;
        return nextComboIndex;
    }

    protected boolean isChargedCombo(int comboIndex) {
        return comboIndex == 0;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel && player instanceof ServerPlayer serverPlayer) handleHoldAction(serverPlayer, stack);
        spawnSpecialParticlesClient(level, player);
        return InteractionResultHolder.pass(stack);
    }

    protected void performNormalAbility(ServerPlayer player, ItemStack stack) {
        if (itemIsOnCooldown(player)) return;
        int nextComboIndex = computeNextComboIndex(stack);
        boolean charged = isChargedCombo(nextComboIndex);
        setComboIndex(stack, nextComboIndex);
        player.getCooldowns().addCooldown(this, getCooldownTicks());
        ServerLevel server = getServerLevel(player.level());
        if (server == null) return;
        Projectile projectile = buildProjectile(server, player, stack, charged);
        if (projectile != null) server.addFreshEntity(projectile);
    }

    protected final Projectile buildProjectile(ServerLevel server, ServerPlayer player, ItemStack stack, boolean charged) {
        if (charged) incrementCharge(stack);
        Projectile projectile = createProjectile(server, player, stack, charged);
        if (projectile != null) {
            projectile.setOwner(player);
            projectile.setPos(player.getEyePosition());
        }
        return projectile;
    }

    protected abstract Projectile createProjectile(ServerLevel server, ServerPlayer player, ItemStack stack, boolean charged);

    protected void performSpecialAbility(ServerPlayer player, ItemStack stack) {
        ServerLevel server = getServerLevel(player.level());
        if (server == null) return;
        applyBuffs(server, player);
        if (canUseDebuff(player)) {
            applyDebuffs(server, player);
            setDebuffCooldown(player, getDebuffCooldownTicks());
        } else reduceDebuffCooldown(player);
    }

    protected boolean canUseDebuff(Player player) {
        return getDebuffCooldown(player) <= 0;
    }

    protected int getDebuffCooldown(Player player) {
        return player.getPersistentData().getInt(getDebuffCooldownTag());
    }

    protected void setDebuffCooldown(Player player, int value) {
        player.getPersistentData().putInt(getDebuffCooldownTag(), value);
    }

    protected void reduceDebuffCooldown(Player player) {
        int cd = getDebuffCooldown(player);
        if (cd > 0) setDebuffCooldown(player, cd - 1);
    }

    protected int getDebuffCooldownTicks() {
        return DEFAULT_DEBUFF_COOLDOWN;
    }

    protected String getDebuffCooldownTag() {
        return "DebuffCD." + getDescriptionId();
    }

    protected abstract void applyBuffs(ServerLevel server, Player player);

    protected abstract void applyDebuffs(ServerLevel server, Player player);

    protected void performUltimateAbility(ServerPlayer player, ItemStack stack) {
        if (!hasMaxCharges(stack)) return;
        if (itemIsOnCooldown(player)) return;
        ultimateAbility(player, stack);
    }

    protected abstract EntityType<? extends AbstractAngel> getAngelType();

    protected void ultimateAbility(ServerPlayer player, ItemStack stack) {
        spawnAngel(player);
        resetCharges(stack);
    }

    private void spawnAngel(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        EntityType<? extends AbstractAngel> angelType = getAngelType();
        if (angelType == null) return;
        AbstractAngel angel = angelType.create(level, null, player.blockPosition(), net.minecraft.world.entity.MobSpawnType.TRIGGERED, false, false);
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
        if (current < MAX_CHARGES) stack.set(BardComponents.CHARGED_ATTACKS_FIRED, current + 1);
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