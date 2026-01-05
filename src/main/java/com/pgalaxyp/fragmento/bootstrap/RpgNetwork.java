package com.pgalaxyp.fragmento.bootstrap;

import com.pgalaxyp.fragmento.rpg.network.RpgPayloadHandler;
import com.pgalaxyp.fragmento.rpg.network.payload.c2s.AbilityIntentPayload;
import com.pgalaxyp.fragmento.rpg.network.payload.c2s.AttackIntentPayload;
import com.pgalaxyp.fragmento.rpg.network.payload.s2c.CombatSnapshotPayload;
import com.pgalaxyp.fragmento.rpg.client.ClientNetworkProxy;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class RpgNetwork {

    private static final String NETWORK_VERSION = "1";

    public static void register(IEventBus modBus) {
        modBus.addListener(RpgNetwork::onRegisterPayloads);
    }

    private static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(NETWORK_VERSION);

        registrar.playToServer(
                AttackIntentPayload.TYPE,
                AttackIntentPayload.STREAM_CODEC,
                RpgPayloadHandler::handleAttackIntent
        );

        registrar.playToServer(
                AbilityIntentPayload.TYPE,
                AbilityIntentPayload.STREAM_CODEC,
                RpgPayloadHandler::handleAbilityIntent
        );

        registrar.playToClient(
                CombatSnapshotPayload.TYPE,
                CombatSnapshotPayload.STREAM_CODEC,
                RpgNetwork::handleCombatSnapshot
        );
    }

    private static void handleCombatSnapshot(CombatSnapshotPayload payload, IPayloadContext context) {
        if (payload == null || context == null) return;
        context.enqueueWork(() -> ClientNetworkProxy.state().apply(payload.snapshot()));
    }

    private RpgNetwork() {}
}