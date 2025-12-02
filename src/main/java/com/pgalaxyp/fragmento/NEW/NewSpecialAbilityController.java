package com.pgalaxyp.fragmento.NEW;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

final class NewSpecialAbilityController {

    static final int SPECIAL_ABILITY_CAST_TIME_TICKS = 35;
    static final int SPECIAL_ABILITY_INITIAL_DELAY_TICKS = 40;
    static final int SPECIAL_ABILITY_COOLDOWN_TICKS = 20;
    static final double SPECIAL_ABILITY_RANGE = 4.5D;

    private static final Map<ServerPlayer, SpecialAbilityChargeState> specialAbilityStates = new WeakHashMap<>();

    private NewSpecialAbilityController() {
    }

    private static final class SpecialAbilityChargeState {
        int ticksSinceStart;
        int ticksSinceLastCast;
        NewFluteSpecialEntity castEntity;
    }

    static boolean isCurrentlyChargingSpecialAbility(ServerPlayer player) {
        return specialAbilityStates.containsKey(player);
    }

    static void handleSpecialAbilityInputState(ServerPlayer player, boolean pressed) {
        if (pressed) {
            startSpecialAbilityCharge(player);
        } else {
            stopSpecialAbilityCharge(player);
        }
    }

    static void tickSpecialAbilityChargingAndCasting() {

        Iterator<Map.Entry<ServerPlayer, SpecialAbilityChargeState>> iterator =
                specialAbilityStates.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry<ServerPlayer, SpecialAbilityChargeState> entry = iterator.next();
            ServerPlayer player = entry.getKey();
            SpecialAbilityChargeState state = entry.getValue();

            ItemStack stack = player.getMainHandItem();
            if (stack.isEmpty() || !(stack.getItem() instanceof NewAbstractWeapon weaponItem)) {
                if (state.castEntity != null) state.castEntity.discard();
                iterator.remove();
                continue;
            }

            Level level = player.level();

            state.ticksSinceStart++;

            weaponItem.onSpecialAbilityTick(player, stack, state.ticksSinceStart);

            double castProgress = Math.min(1.0D, state.ticksSinceStart / (double) SPECIAL_ABILITY_CAST_TIME_TICKS);
            double radius = SPECIAL_ABILITY_RANGE * castProgress * 2;

            if (!level.isClientSide() && state.ticksSinceStart == 1) {

                NewFluteSpecialEntity cast = new NewFluteSpecialEntity(
                        EntitiesRegistry.NEW_FLUTE_SPECIAL_ENTITY.get(),
                        level
                );

                cast.setPos(player.getX(), player.getY(), player.getZ());
                cast.setYRot(player.getYRot());
                cast.setXRot(player.getXRot());

                level.addFreshEntity(cast);

                state.castEntity = cast;
            }

            if (state.castEntity != null && state.castEntity.isAlive()) {
                state.castEntity.setRadius((float) radius);
            }

            if (state.ticksSinceStart < SPECIAL_ABILITY_INITIAL_DELAY_TICKS) {
                continue;
            }

            if (state.ticksSinceLastCast < 0) {
                state.ticksSinceLastCast = 0;
            } else {
                state.ticksSinceLastCast++;
            }

            if (state.ticksSinceLastCast >= SPECIAL_ABILITY_COOLDOWN_TICKS) {
                state.ticksSinceLastCast = 0;
                weaponItem.useSpecialAbilityOnNearbyTargets(player, stack);
            }
        }
    }

    private static void startSpecialAbilityCharge(ServerPlayer player) {
        SpecialAbilityChargeState state = new SpecialAbilityChargeState();
        state.ticksSinceStart = 0;
        state.ticksSinceLastCast = -1;
        specialAbilityStates.put(player, state);
    }

    private static void stopSpecialAbilityCharge(ServerPlayer player) {
        SpecialAbilityChargeState state = specialAbilityStates.remove(player);

        if (state != null && state.castEntity != null && state.castEntity.isAlive()) {
            state.castEntity.discard();
        }
    }
}
