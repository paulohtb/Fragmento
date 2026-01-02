package com.pgalaxyp.fragmento.combat.engine.network;

import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.combat.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.combat.engine.runtime.ServerCombatSystem;
import com.pgalaxyp.fragmento.combat.network.payload.c2s.AbilityIntentPayload;
import com.pgalaxyp.fragmento.combat.network.payload.c2s.AttackIntentPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ServerCombatPayloadHandler {

    public static void handleAttackIntent(AttackIntentPayload payload, IPayloadContext context) {
        if (payload == null || context == null) {
            return;
        } else {
            context.player();
        }
        if (!(context.player() instanceof net.minecraft.server.level.ServerPlayer sp)) {
            return;
        }
        AttackIntent intent = payload.intent();
        if (intent == null) {
            return;
        }
        ServerCombatSystem.get().onAttackIntent(sp, intent);
    }

    public static void handleAbilityIntent(AbilityIntentPayload payload, IPayloadContext context) {
        if (payload == null || context == null) {
            return;
        } else {
            context.player();
        }
        if (!(context.player() instanceof net.minecraft.server.level.ServerPlayer sp)) {
            return;
        }
        AbilityIntent intent = payload.intent();
        if (intent == null) {
            return;
        }
        ServerCombatSystem.get().onAbilityIntent(sp, intent);
    }

    private ServerCombatPayloadHandler() {}
}