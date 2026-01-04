package com.pgalaxyp.fragmento.combat.client.proxy;

import com.pgalaxyp.fragmento.bootstrap.logging.FragmentoLog;
import com.pgalaxyp.fragmento.combat.client.network.CombatSnapshotReceiver;
import com.pgalaxyp.fragmento.combat.client.state.ClientCombatViewState;
import com.pgalaxyp.fragmento.combat.network.payload.s2c.CombatSnapshotPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class CombatClientProxy {

    private static final ClientCombatViewState STATE = new ClientCombatViewState();
    private static final CombatSnapshotReceiver RECEIVER = new CombatSnapshotReceiver(STATE);

    public static void handleSnapshot(CombatSnapshotPayload payload, IPayloadContext context) {
        if (payload == null || context == null) {
            FragmentoLog.snapshot("client handleSnapshot ignore, payloadNull={} contextNull={}", payload == null, context == null);
            return;
        }

        long v = payload.snapshot() != null && payload.snapshot().version() != null ? payload.snapshot().version().value() : -1L;

        FragmentoLog.snapshot(
                "client handleSnapshot enqueueWork, version={} ctx.playerPresent={}",
                v,
                context.player() != null
        );

        context.enqueueWork(() -> {
            try {
                RECEIVER.apply(payload);
            } catch (Throwable t) {
                FragmentoLog.snapshotEx(t, "client handleSnapshot crashed, version={}", v);
            }
        });
    }

    public static ClientCombatViewState state() {
        return STATE;
    }

    private CombatClientProxy() {}
}