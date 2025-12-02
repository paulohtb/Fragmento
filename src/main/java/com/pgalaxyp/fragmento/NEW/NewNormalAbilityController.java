package com.pgalaxyp.fragmento.NEW;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

final class NewNormalAbilityController {

    static final int NORMAL_ABILITY_COOLDOWN_TICKS = 20;
    private static final int NORMAL_ABILITY_PROJECTILE_DELAY_TICKS = 2;

    private static final CountdownMap<ServerPlayer> normalAbilityCooldowns = new CountdownMap<>();

    private NewNormalAbilityController() {
    }

    static boolean updateNormalAbilityChargeAndCheckReady(ItemStack stack) {
        int currentHits = stack.getOrDefault(NewDataComponents.NORMAL_ABILITY_HIT_COUNT.get(), 0) + 1;
        int requiredHits = NewAbstractProjectile.getHitsRequiredForChargedState();
        boolean reached = currentHits >= requiredHits;

        stack.set(
                NewDataComponents.NORMAL_ABILITY_HIT_COUNT.get(),
                reached ? 0 : currentHits
        );

        return reached;
    }

    static void handleNormalAbilityInput(ServerPlayer player) {
        if (NewSpecialAbilityController.isCurrentlyChargingSpecialAbility(player)) return;
        if (normalAbilityCooldowns.isActive(player)) return;

        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) return;

        if (stack.getItem() instanceof NewAbstractWeapon weapon) {
            weapon.useNormalAbility(player, stack);
            normalAbilityCooldowns.start(player, NORMAL_ABILITY_COOLDOWN_TICKS);
        }
    }

    static void tickNormalAbilityCooldowns() {
        normalAbilityCooldowns.tick();
    }

    static void executeNormalAbilityProjectileAttack(
            NewAbstractWeapon weapon,
            ServerPlayer player,
            ItemStack stack,
            NewAbstractWeapon.TriFunction<Level, LivingEntity, Boolean, Projectile> factory
    ) {
        if (factory == null) return;

        Level level = player.level();
        if (level.isClientSide()) return;

        boolean charged = weapon.updateNormalAbilityChargeAndCheckReady(stack);

        weapon.onNormalAbilityFired(player, stack, charged);

        Projectile projectile = factory.apply(level, player, charged);
        if (projectile == null) return;

        if (projectile instanceof NewAbstractProjectile p) {
            p.setSpawnDelayTicks(NORMAL_ABILITY_PROJECTILE_DELAY_TICKS);
        }

        Vec3 look = player.getLookAngle().normalize();
        Vec3 frontOffset = look.scale(1.25);

        double spawnX = player.getX() + frontOffset.x;
        double spawnY = player.getY() + 1.1 + frontOffset.y;
        double spawnZ = player.getZ() + frontOffset.z;

        projectile.setPos(spawnX, spawnY, spawnZ);

        Vec3 velocity = look.scale(2.5f);
        projectile.setDeltaMovement(velocity);

        NewSoundWaveEntity wave = new NewSoundWaveEntity(EntitiesRegistry.NEW_SOUNDWAVE_ENTITY.get(), level);
        wave.setPos(spawnX, spawnY, spawnZ);

        wave.setDeltaMovement(velocity);

        level.addFreshEntity(wave);
        level.addFreshEntity(projectile);
    }
}
