package com.pgalaxyp.fragmento.NEW;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public abstract class NewAbstractWeapon extends Item {

    private final TriFunction<Level, LivingEntity, Boolean, Projectile> projectileFactory;

    public NewAbstractWeapon(Properties properties, TriFunction<Level, LivingEntity, Boolean, Projectile> projectileFactory) {
        super(properties.stacksTo(1));
        this.projectileFactory = projectileFactory;
    }

    public static int getNormalAbilityClientCooldownTicks() {
        return NewNormalAbilityController.NORMAL_ABILITY_COOLDOWN_TICKS;
    }

    public static void handleNormalAbilityInput(ServerPlayer player) {
        NewNormalAbilityController.handleNormalAbilityInput(player);
    }

    public static void handleSpecialAbilityInputState(ServerPlayer player, boolean pressed) {
        NewSpecialAbilityController.handleSpecialAbilityInputState(player, pressed);
    }

    public static void serverTickUpdateAllAbilityWeapons() {
        NewSpecialAbilityController.tickSpecialAbilityChargingAndCasting();
        NewNormalAbilityController.tickNormalAbilityCooldowns();
    }

    public void useNormalAbility(ServerPlayer player, ItemStack stack) {
        NewNormalAbilityController.executeNormalAbilityProjectileAttack(this, player, stack, this.projectileFactory);
    }

    public void useSpecialAbilityOnNearbyTargets(ServerPlayer player, ItemStack stack) {
        Level level = player.level();
        if (level.isClientSide()) {
            return;
        }

        double range = NewSpecialAbilityController.SPECIAL_ABILITY_RANGE;
        double rangeSquared = range * range;

        AABB searchBox = player.getBoundingBox().inflate(range);
        var targets = level.getEntitiesOfClass(
                LivingEntity.class,
                searchBox,
                entity -> entity.isAlive() && entity != player
        );

        for (LivingEntity target : targets) {
            if (target.distanceToSqr(player) <= rangeSquared) {
                this.applySpecialAbilityEffectToTarget(player, stack, target);
            }
        }
    }

    protected void applySpecialAbilityEffectToTarget(ServerPlayer player, ItemStack stack, LivingEntity target) {
    }

    protected boolean updateNormalAbilityChargeAndCheckReady(ItemStack stack) {
        return NewNormalAbilityController.updateNormalAbilityChargeAndCheckReady(stack);
    }

    @FunctionalInterface
    public interface TriFunction<A, B, C, R> {
        R apply(A a, B b, C c);
    }
}
