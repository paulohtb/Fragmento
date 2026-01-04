package com.pgalaxyp.fragmento.combat.network;

import com.pgalaxyp.fragmento.combat.client.proxy.CombatClientProxy;
import com.pgalaxyp.fragmento.combat.engine.network.ServerCombatPayloadHandler;
import com.pgalaxyp.fragmento.combat.network.payload.c2s.AbilityIntentPayload;
import com.pgalaxyp.fragmento.combat.network.payload.c2s.AttackIntentPayload;
import com.pgalaxyp.fragmento.combat.network.payload.s2c.CombatSnapshotPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class FragmentoNetwork {

    public static void register(IEventBus modBus) {
        modBus.addListener(FragmentoNetwork::onRegister);
    }

    private static void onRegister(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                AttackIntentPayload.TYPE,
                AttackIntentPayload.STREAM_CODEC,
                ServerCombatPayloadHandler::handleAttackIntent
        );

        registrar.playToServer(
                AbilityIntentPayload.TYPE,
                AbilityIntentPayload.STREAM_CODEC,
                ServerCombatPayloadHandler::handleAbilityIntent
        );

        registrar.playToClient(
                CombatSnapshotPayload.TYPE,
                CombatSnapshotPayload.STREAM_CODEC,
                CombatClientProxy::handleSnapshot
        );
    }

    private FragmentoNetwork() {}
}