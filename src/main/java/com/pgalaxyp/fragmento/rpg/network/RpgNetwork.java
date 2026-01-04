package com.pgalaxyp.fragmento.rpg.network;

import com.pgalaxyp.fragmento.rpg.client.network.ClientNetworkProxy;
import com.pgalaxyp.fragmento.rpg.network.payload.c2s.AbilityIntentPayload;
import com.pgalaxyp.fragmento.rpg.network.payload.c2s.AttackIntentPayload;
import com.pgalaxyp.fragmento.rpg.network.payload.s2c.CombatSnapshotPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class RpgNetwork {

    public static void register(IEventBus modBus) {
        modBus.addListener(RpgNetwork::onRegister);
    }

    private static void onRegister(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

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
                ClientNetworkProxy::handleSnapshot
        );
    }

    private RpgNetwork() {}
}