package com.pgalaxyp.fragmento.item.bard_weapon;

import com.pgalaxyp.fragmento.entity.bard.angels.AbstractAngel;
import com.pgalaxyp.fragmento.util.BardComponents;
import com.pgalaxyp.fragmento.util.FinalComboAware;
import com.pgalaxyp.fragmento.util.NormalAbilityComboHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractBardWeapon extends Item {

    private static final int MAX_CHARGES = 5;
    protected static final double SPECIAL_RANGE = 4.0D;

    public AbstractBardWeapon(Properties properties) {
        super(properties);
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

    protected abstract EntityType<? extends AbstractAngel> getAngelType();

    protected AABB getSpecialRangeAABB(Player player) {
        return player.getBoundingBox().inflate(SPECIAL_RANGE);
    }

    private void spawnAngel(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        EntityType<? extends AbstractAngel> angelType = getAngelType();
        if (angelType != null) {
            AbstractAngel angel = angelType.create(level, null, player.blockPosition(), MobSpawnType.TRIGGERED, false, false);
            if (angel != null) {
                angel.setOwner(player);
                angel.setPos(player.getX(), player.getY() + 2, player.getZ());
                level.addFreshEntity(angel);
            }
        }
    }

    protected void ultimateAbility(ServerPlayer player, ItemStack stack) {
        spawnAngel(player);
        resetCharges(stack);
    }

    protected abstract Projectile createProjectile(ServerLevel server, ServerPlayer player, ItemStack stack, boolean charged);

    protected final Projectile buildProjectile(ServerLevel server, ServerPlayer player, ItemStack stack, boolean charged) {
        if (charged) {
            incrementCharge(stack);
        }
        Projectile projectile = createProjectile(server, player, stack, charged);
        if (projectile != null) {
            projectile.setOwner(player);
            if (projectile instanceof FinalComboAware fc) {
                fc.setFinalCombo(charged);
            }
            projectile.setPos(player.getEyePosition());
        }
        return projectile;
    }

    public static void normalAbility(ServerPlayer player, ItemStack stack) {
        if (!(stack.getItem() instanceof AbstractBardWeapon weapon)) return;
        if (player.getCooldowns().isOnCooldown(weapon)) return;

        player.getCooldowns().addCooldown(weapon, 15);

        var combo = NormalAbilityComboHandler.fromItemStack(stack);
        combo.advanceCombo();
        combo.writeToItemStack(stack);

        Level level = player.level();
        if (!(level instanceof ServerLevel server)) return;

        boolean charged = combo.isFinalComboHit();
        Projectile projectile = weapon.buildProjectile(server, player, stack, charged);
        if (projectile != null) {
            server.addFreshEntity(projectile);
        }
    }

    protected abstract void specialAbility(ServerLevel level, ServerPlayer player, ItemStack stack);

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
            if (hasMaxCharges(stack)) {
                ultimateAbility(serverPlayer, stack);
            } else {
                specialAbility(serverLevel, serverPlayer, stack);
            }
        }

        return InteractionResultHolder.pass(stack);
    }
}