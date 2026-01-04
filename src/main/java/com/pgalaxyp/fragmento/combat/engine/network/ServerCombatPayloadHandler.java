package com.pgalaxyp.fragmento.combat.engine.network;

import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.combat.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.combat.engine.runtime.FragmentoCombatRuntime;
import com.pgalaxyp.fragmento.combat.network.payload.c2s.AbilityIntentPayload;
import com.pgalaxyp.fragmento.combat.network.payload.c2s.AttackIntentPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ServerCombatPayloadHandler {

    public static void handleAttackIntent(AttackIntentPayload payload, IPayloadContext context) {
        if (payload == null || context == null) {
            return;
        }
        if (!(context.player() instanceof ServerPlayer player)) {
            return;
        }

        AttackIntent intent = payload.intent();
        if (intent == null) {
            return;
        }

        context.enqueueWork(() -> FragmentoCombatRuntime.get().onAttackIntent(player, intent));
    }

    public static void handleAbilityIntent(AbilityIntentPayload payload, IPayloadContext context) {
        if (payload == null || context == null) {
            return;
        }
        if (!(context.player() instanceof ServerPlayer player)) {
            return;
        }

        AbilityIntent intent = payload.intent();
        if (intent == null) {
            return;
        }

        context.enqueueWork(() -> FragmentoCombatRuntime.get().onAbilityIntent(player, intent));
    }

    private ServerCombatPayloadHandler() {}
}