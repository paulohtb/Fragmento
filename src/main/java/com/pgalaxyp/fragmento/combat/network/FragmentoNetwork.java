package com.pgalaxyp.fragmento.combat.network;

import com.pgalaxyp.fragmento.combat.client.network.CombatSnapshotReceiver;
import com.pgalaxyp.fragmento.combat.client.state.ClientCombatViewState;
import com.pgalaxyp.fragmento.combat.engine.network.ServerCombatPayloadHandler;
import com.pgalaxyp.fragmento.combat.network.payload.c2s.AbilityIntentPayload;
import com.pgalaxyp.fragmento.combat.network.payload.c2s.AttackIntentPayload;
import com.pgalaxyp.fragmento.combat.network.payload.s2c.CombatSnapshotPayload;
import com.pgalaxyp.fragmento.combat.network.payload.s2c.VisualCuePayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.MOD)
public final class FragmentoNetwork {

    private static final ClientCombatViewState CLIENT_STATE = new ClientCombatViewState();
    private static final CombatSnapshotReceiver CLIENT_SNAPSHOT_RECEIVER = new CombatSnapshotReceiver(CLIENT_STATE);

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
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
                (payload, context) -> CLIENT_SNAPSHOT_RECEIVER.apply(payload)
        );

        registrar.playToClient(
                VisualCuePayload.TYPE,
                VisualCuePayload.STREAM_CODEC,
                (payload, context) -> {}
        );
    }

    private FragmentoNetwork() {}
}