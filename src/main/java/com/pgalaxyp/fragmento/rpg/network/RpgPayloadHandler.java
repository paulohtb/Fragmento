package com.pgalaxyp.fragmento.rpg.network;

import com.pgalaxyp.fragmento.rpg.network.payload.c2s.AbilityIntentPayload;
import com.pgalaxyp.fragmento.rpg.network.payload.c2s.AttackIntentPayload;
import com.pgalaxyp.fragmento.rpg.runtime.RpgRuntime;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class RpgPayloadHandler {

    private static RpgRuntime runtime;

    public static void bindRuntime(RpgRuntime rt) {
        runtime = rt;
    }

    public static void handleAttackIntent(AttackIntentPayload payload, IPayloadContext context) {
        if (payload == null || context == null) return;
        if (!(context.player() instanceof ServerPlayer player)) return;
        if (runtime == null) return;

        context.enqueueWork(() ->
                runtime.onAttackIntent(player, payload.intent())
        );
    }

    public static void handleAbilityIntent(AbilityIntentPayload payload, IPayloadContext context) {
        if (payload == null || context == null) return;
        if (!(context.player() instanceof ServerPlayer player)) return;
        if (runtime == null) return;

        context.enqueueWork(() ->
                runtime.onAbilityIntent(player, payload.intent())
        );
    }

    private RpgPayloadHandler() {}
}