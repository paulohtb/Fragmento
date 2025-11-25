package com.pgalaxyp.fragmento.NEW;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

final class NewNormalAbilityController {

    static final int NORMAL_ABILITY_COOLDOWN_TICKS = 20;

    private static final CountdownMap<ServerPlayer> normalAbilityCooldowns = new CountdownMap<>();

    private NewNormalAbilityController() {
    }

    static boolean updateNormalAbilityChargeAndCheckReady(ItemStack stack) {
        int currentHits = stack.getOrDefault(NewDataComponents.NORMAL_ABILITY_HIT_COUNT.get(), 0) + 1;
        int requiredHits = NewAbstractProjectile.getHitsRequiredForChargedState();
        boolean reachedChargedState = currentHits >= requiredHits;
        if (reachedChargedState) {
            stack.set(NewDataComponents.NORMAL_ABILITY_HIT_COUNT.get(), 0);
        } else {
            stack.set(NewDataComponents.NORMAL_ABILITY_HIT_COUNT.get(), currentHits);
        }
        return reachedChargedState;
    }

    static void handleNormalAbilityInput(ServerPlayer player) {
        if (NewSpecialAbilityController.isCurrentlyChargingSpecialAbility(player)) {
            return;
        }
        if (normalAbilityCooldowns.isActive(player)) {
            return;
        }

        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) {
            return;
        }

        if (stack.getItem() instanceof NewAbstractWeapon weaponItem) {
            weaponItem.useNormalAbility(player, stack);
            normalAbilityCooldowns.start(player, NORMAL_ABILITY_COOLDOWN_TICKS);
        }
    }

    static void tickNormalAbilityCooldowns() {
        normalAbilityCooldowns.tick();
    }

    static void executeNormalAbilityProjectileAttack(
            NewAbstractWeapon weaponItem,
            ServerPlayer player,
            ItemStack stack,
            NewAbstractWeapon.TriFunction<Level, LivingEntity, Boolean, Projectile> projectileFactory
    ) {
        if (projectileFactory == null) {
            return;
        }

        Level level = player.level();
        if (level.isClientSide()) {
            return;
        }

        boolean shouldFireChargedProjectile = weaponItem.updateNormalAbilityChargeAndCheckReady(stack);

        Projectile projectile = projectileFactory.apply(level, player, shouldFireChargedProjectile);
        if (projectile == null) {
            return;
        }

        projectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());

        Vec3 lookDirection = player.getLookAngle();
        if (projectile instanceof NewAbstractProjectile abilityProjectile) {
            abilityProjectile.shootInStraightDirection(lookDirection, shouldFireChargedProjectile ? 2.5f : 1.5f, 0.0f);
        } else {
            projectile.setDeltaMovement(lookDirection.scale(1.5f));
        }

        level.addFreshEntity(projectile);
    }
}
