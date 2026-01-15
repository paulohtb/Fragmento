package com.pgalaxyp.fragmento.combat.platform.neoforge.net.wire;

import com.pgalaxyp.fragmento.combat.ports.ClientInboundPort;
import com.pgalaxyp.fragmento.combat.ports.ServerIntentReceiverPort;
import java.util.concurrent.atomic.AtomicReference;

public final class NfRuntimeRefs {

    private static final AtomicReference<ServerIntentReceiverPort> serverReceiver = new AtomicReference<>();
    private static final AtomicReference<ClientInboundPort> clientInbound = new AtomicReference<>();

    public static void setServerReceiver(ServerIntentReceiverPort port) {
        serverReceiver.set(port);
    }

    public static void clearServerReceiver() {
        serverReceiver.set(null);
    }

    public static ServerIntentReceiverPort serverReceiver() {
        return serverReceiver.get();
    }

    public static void setClientInbound(ClientInboundPort port) {
        clientInbound.set(port);
    }

    public static void clearClientInbound() {
        clientInbound.set(null);
    }

    public static ClientInboundPort clientInbound() {
        return clientInbound.get();
    }

    private NfRuntimeRefs() {}
}