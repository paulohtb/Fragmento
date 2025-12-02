package com.pgalaxyp.fragmento.NEW;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public abstract class NewAbstractWeapon extends Item {

    final TriFunction<Level, LivingEntity, Boolean, Projectile> projectileFactory;

    public NewAbstractWeapon(Properties props, TriFunction<Level, LivingEntity, Boolean, Projectile> factory) {
        super(props.stacksTo(1));
        this.projectileFactory = factory;
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

    protected boolean updateNormalAbilityChargeAndCheckReady(ItemStack stack) {
        return NewNormalAbilityController.updateNormalAbilityChargeAndCheckReady(stack);
    }

    public void useSpecialAbilityOnNearbyTargets(ServerPlayer player, ItemStack stack) {
        Level level = player.level();
        if (level.isClientSide()) return;

        this.applySpecialAbilityEffectToTarget(player, stack, player);

        double range = NewSpecialAbilityController.SPECIAL_ABILITY_RANGE;
        double dist2 = range * range;

        AABB box = player.getBoundingBox().inflate(range);

        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, box, e -> e.isAlive() && e != player)) {
            if (target.distanceToSqr(player) <= dist2) {
                this.applySpecialAbilityEffectToTarget(player, stack, target);
            }
        }
    }

    protected void applySpecialAbilityEffectToTarget(ServerPlayer player, ItemStack stack, LivingEntity target) {
    }

    protected void onNormalAbilityFired(ServerPlayer player, ItemStack stack, boolean charged) {
    }

    protected void onSpecialAbilityTick(ServerPlayer player, ItemStack stack, int ticks) {
    }

    @FunctionalInterface
    public interface TriFunction<A, B, C, R> {
        R apply(A a, B b, C c);
    }
}
