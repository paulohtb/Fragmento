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
    private static final int PARTICLE_EMIT_INTERVAL_TICKS = 2;

    private static final Map<ServerPlayer, SpecialAbilityChargeState> specialAbilityStates = new WeakHashMap<>();

    private NewSpecialAbilityController() {
    }

    private static final class SpecialAbilityChargeState {
        int ticksSinceStart;
        int ticksSinceLastCast;
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
        Iterator<Map.Entry<ServerPlayer, SpecialAbilityChargeState>> iterator = specialAbilityStates.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<ServerPlayer, SpecialAbilityChargeState> entry = iterator.next();
            ServerPlayer player = entry.getKey();
            SpecialAbilityChargeState state = entry.getValue();

            ItemStack stack = player.getMainHandItem();
            if (stack.isEmpty() || !(stack.getItem() instanceof NewAbstractWeapon weaponItem)) {
                iterator.remove();
                continue;
            }

            Level level = player.level();

            state.ticksSinceStart = state.ticksSinceStart + 1;

            double castProgress = Math.min(1.0D, state.ticksSinceStart / (double) SPECIAL_ABILITY_CAST_TIME_TICKS);
            double radius = SPECIAL_ABILITY_RANGE * castProgress;

            if (!level.isClientSide() && state.ticksSinceStart % PARTICLE_EMIT_INTERVAL_TICKS == 0) {
                NewParticleEffects.spawnSpecialAbilityRingParticles(player, radius, SPECIAL_ABILITY_RANGE);
            }

            if (state.ticksSinceStart < SPECIAL_ABILITY_INITIAL_DELAY_TICKS) {
                continue;
            }

            if (state.ticksSinceLastCast < 0) {
                state.ticksSinceLastCast = 0;
            } else {
                state.ticksSinceLastCast = state.ticksSinceLastCast + 1;
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
        specialAbilityStates.remove(player);
    }
}