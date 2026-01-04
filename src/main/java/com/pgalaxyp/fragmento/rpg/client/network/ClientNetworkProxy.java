package com.pgalaxyp.fragmento.rpg.client.network;

import com.pgalaxyp.fragmento.rpg.client.state.ClientViewState;
import com.pgalaxyp.fragmento.rpg.network.payload.s2c.CombatSnapshotPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ClientNetworkProxy {

    private static final ClientViewState STATE = new ClientViewState();

    public static void handleSnapshot(CombatSnapshotPayload payload, IPayloadContext context) {
        if (payload == null || payload.snapshot() == null) return;

        context.enqueueWork(() -> STATE.apply(payload.snapshot()));
    }

    public static ClientViewState state() {
        return STATE;
    }

    private ClientNetworkProxy() {}
}