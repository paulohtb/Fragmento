package com.pgalaxyp.fragmento.combat.client.network;

import com.pgalaxyp.fragmento.combat.client.state.ClientCombatViewState;
import com.pgalaxyp.fragmento.combat.network.payload.s2c.CombatSnapshotPayload;

public final class CombatSnapshotReceiver {

    private final ClientCombatViewState state;

    public CombatSnapshotReceiver(ClientCombatViewState state) {
        this.state = state;
    }

    public void apply(CombatSnapshotPayload payload) {
        if (payload == null) {
            return;
        }
        state.apply(payload.snapshot());
    }
}