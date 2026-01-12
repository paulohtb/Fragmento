package com.pgalaxyp.fragmento.rpg.platform.neoforge.net.wire;

import com.pgalaxyp.fragmento.rpg.core.events.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.net.codec.NeoForgeNetCodec;
import com.pgalaxyp.fragmento.rpg.ports.ClientInboundPort;
import com.pgalaxyp.fragmento.rpg.ports.ServerIntentReceiverPort;
import com.pgalaxyp.fragmento.rpg.ports.dto.GameSnapshot;
import java.util.List;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class NeoForgeNetWire {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar r = event.registrar("1");
        r.playToServer(C2SIntentPayload.TYPE, C2SIntentPayload.STREAM_CODEC, NeoForgeNetWire::handleIntent);
        r.playToClient(S2CSnapshotPayload.TYPE, S2CSnapshotPayload.STREAM_CODEC, NeoForgeNetWire::handleSnapshot);
        r.playToClient(S2CEventsPayload.TYPE, S2CEventsPayload.STREAM_CODEC, NeoForgeNetWire::handleEvents);
    }

    public static void sendIntentToServer(IntentEnvelope env) {
        if (env == null) {
            throw new IllegalArgumentException();
        }
        byte[] bytes = NeoForgeNetCodec.encodeIntent(env);
        PacketDistributor.sendToServer(new C2SIntentPayload(bytes));
    }

    public static void sendSnapshotToAll(byte[] encodedSnapshot) {
        PacketDistributor.sendToAllPlayers(new S2CSnapshotPayload(encodedSnapshot));
    }

    public static void sendEventsToAll(byte[] encodedEvents) {
        PacketDistributor.sendToAllPlayers(new S2CEventsPayload(encodedEvents));
    }

    public static byte[] encodeSnapshot(GameSnapshot snapshot) {
        return NeoForgeNetCodec.encodeSnapshot(snapshot);
    }

    public static byte[] encodeEvents(List<DomainEvent> events) {
        return NeoForgeNetCodec.encodeEvents(events);
    }

    private static void handleIntent(C2SIntentPayload payload, IPayloadContext context) {
        IntentEnvelope env = NeoForgeNetCodec.decodeIntent(payload.data());
        ServerIntentReceiverPort recv = NeoForgeNetRuntimeRefs.serverReceiver();
        if (recv == null) {
            return;
        }
        context.enqueueWork(() -> recv.enqueue(env));
    }

    private static void handleSnapshot(S2CSnapshotPayload payload, IPayloadContext context) {
        GameSnapshot snap = NeoForgeNetCodec.decodeSnapshot(payload.data());
        ClientInboundPort inbound = NeoForgeNetRuntimeRefs.clientInbound();
        if (inbound == null) {
            return;
        }
        context.enqueueWork(() -> inbound.acceptSnapshot(snap));
    }

    private static void handleEvents(S2CEventsPayload payload, IPayloadContext context) {
        List<DomainEvent> events = NeoForgeNetCodec.decodeEvents(payload.data());
        ClientInboundPort inbound = NeoForgeNetRuntimeRefs.clientInbound();
        if (inbound == null) {
            return;
        }
        context.enqueueWork(() -> inbound.acceptEvents(events));
    }

    private NeoForgeNetWire() {}
}