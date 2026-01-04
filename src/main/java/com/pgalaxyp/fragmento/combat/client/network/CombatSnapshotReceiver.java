package com.pgalaxyp.fragmento.combat.client.network;

import com.pgalaxyp.fragmento.bootstrap.logging.FragmentoLog;
import com.pgalaxyp.fragmento.combat.client.state.ClientCombatViewState;
import com.pgalaxyp.fragmento.combat.network.payload.s2c.CombatSnapshotPayload;
import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshot;

public final class CombatSnapshotReceiver {

    private final ClientCombatViewState state;

    public CombatSnapshotReceiver(ClientCombatViewState state) {
        this.state = state;
    }

    public void apply(CombatSnapshotPayload payload) {
        if (payload == null) {
            FragmentoLog.snapshot("client snapshot receiver ignore, payloadNull=true");
            return;
        }

        CombatSnapshot snap = payload.snapshot();
        if (snap == null || snap.version() == null) {
            FragmentoLog.snapshot("client snapshot receiver ignore, snapshotNull={} versionNull={}", snap == null, snap != null && snap.version() == null);
            return;
        }

        FragmentoLog.snapshot(
                "client snapshot apply, version={}",
                snap.version().value()
        );

        state.apply(snap);
    }
}