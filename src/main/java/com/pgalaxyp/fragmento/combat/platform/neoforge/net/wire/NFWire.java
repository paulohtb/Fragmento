package com.pgalaxyp.fragmento.combat.platform.neoforge.net.wire;

import com.pgalaxyp.fragmento.combat.core.events.event.DomainEvent;
import com.pgalaxyp.fragmento.combat.core.events.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.combat.platform.neoforge.net.codec.NFCodec;
import com.pgalaxyp.fragmento.combat.ports.ClientInboundPort;
import com.pgalaxyp.fragmento.combat.ports.ServerIntentReceiverPort;
import com.pgalaxyp.fragmento.combat.ports.dto.GameSnapshot;
import java.util.List;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class NFWire {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar r = event.registrar("1");
        r.playToServer(C2SIntentPayload.TYPE, C2SIntentPayload.STREAM_CODEC, NFWire::handleIntent);
        r.playToClient(S2CSnapshotPayload.TYPE, S2CSnapshotPayload.STREAM_CODEC, NFWire::handleSnapshot);
        r.playToClient(S2CEventsPayload.TYPE, S2CEventsPayload.STREAM_CODEC, NFWire::handleEvents);
    }

    public static void sendIntentToServer(IntentEnvelope env) {
        if (env == null) {
            throw new IllegalArgumentException();
        }
        byte[] bytes = NFCodec.encodeIntent(env);
        PacketDistributor.sendToServer(new C2SIntentPayload(bytes));
    }

    public static void sendSnapshotToAll(byte[] encodedSnapshot) {
        PacketDistributor.sendToAllPlayers(new S2CSnapshotPayload(encodedSnapshot));
    }

    public static void sendEventsToAll(byte[] encodedEvents) {
        PacketDistributor.sendToAllPlayers(new S2CEventsPayload(encodedEvents));
    }

    public static byte[] encodeSnapshot(GameSnapshot snapshot) {
        return NFCodec.encodeSnapshot(snapshot);
    }

    public static byte[] encodeEvents(List<DomainEvent> events) {
        return NFCodec.encodeEvents(events);
    }

    private static void handleIntent(C2SIntentPayload payload, IPayloadContext context) {
        IntentEnvelope env = NFCodec.decodeIntent(payload.data());
        ServerIntentReceiverPort recv = NFRuntimeRefs.serverReceiver();
        if (recv == null) {
            return;
        }
        context.enqueueWork(() -> recv.enqueue(env));
    }

    private static void handleSnapshot(S2CSnapshotPayload payload, IPayloadContext context) {
        GameSnapshot snap = NFCodec.decodeSnapshot(payload.data());
        ClientInboundPort inbound = NFRuntimeRefs.clientInbound();
        if (inbound == null) {
            return;
        }
        context.enqueueWork(() -> inbound.acceptSnapshot(snap));
    }

    private static void handleEvents(S2CEventsPayload payload, IPayloadContext context) {
        List<DomainEvent> events = NFCodec.decodeEvents(payload.data());
        ClientInboundPort inbound = NFRuntimeRefs.clientInbound();
        if (inbound == null) {
            return;
        }
        context.enqueueWork(() -> inbound.acceptEvents(events));
    }

    private NFWire() {}
}