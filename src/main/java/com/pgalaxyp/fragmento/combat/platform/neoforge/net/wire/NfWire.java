package com.pgalaxyp.fragmento.combat.platform.neoforge.net.wire;

import com.pgalaxyp.fragmento.combat.intent.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.net.codec.*;
import com.pgalaxyp.fragmento.combat.ports.*;
import com.pgalaxyp.fragmento.combat.transport.snapshot.api.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.network.event.*;
import net.neoforged.neoforge.network.handling.*;
import net.neoforged.neoforge.network.registration.*;

public final class NfWire {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar r = event.registrar("2");
        r.playToServer(C2SIntentPayload.TYPE, C2SIntentPayload.STREAM_CODEC, NfWire::handleIntent);
        r.playToClient(S2CSnapshotPayload.TYPE, S2CSnapshotPayload.STREAM_CODEC, NfWire::handleSnapshot);
    }

    public static void sendIntentToServer(IntentEnvelope env) {
        if (env == null) throw new IllegalArgumentException();
        PacketDistributor.sendToServer(new C2SIntentPayload(NfCodec.encodeIntent(env)));
    }

    public static void sendSnapshotToAll(byte[] encodedSnapshot) { PacketDistributor.sendToAllPlayers(new S2CSnapshotPayload(encodedSnapshot)); }

    public static byte[] encodeSnapshot(GameSnapshot snapshot) { return NfCodec.encodeSnapshot(snapshot); }

    private static void handleIntent(C2SIntentPayload payload, IPayloadContext context) {
        IntentEnvelope env = NfCodec.decodeIntent(payload.data());
        ServerIntentReceiverPort recv = NfRuntimeRefs.serverReceiver();
        if (recv != null) context.enqueueWork(() -> recv.enqueue(env));
    }

    private static void handleSnapshot(S2CSnapshotPayload payload, IPayloadContext context) {
        GameSnapshot snap = NfCodec.decodeSnapshot(payload.data());
        ClientInboundPort inbound = NfRuntimeRefs.clientInbound();
        if (inbound != null) context.enqueueWork(() -> inbound.acceptSnapshot(snap));
    }

    private NfWire() {}
}
