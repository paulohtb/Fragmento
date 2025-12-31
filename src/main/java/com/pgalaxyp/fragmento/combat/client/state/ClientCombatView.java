package com.pgalaxyp.fragmento.combat.client.state;

import com.pgalaxyp.fragmento.combat.network.CombatSnapshotPayload;
import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshot;

public final class ClientCombatView {

    private CombatSnapshot lastSnapshot;

    public void apply(CombatSnapshotPayload payload) {
        if (payload == null || payload.snapshot() == null) {
            return;
        }

        CombatSnapshot next = payload.snapshot();
        if (lastSnapshot == null || next.version().isAfter(lastSnapshot.version())) {
            lastSnapshot = next;
        }
    }

    public CombatSnapshot current() {
        return lastSnapshot;
    }
}