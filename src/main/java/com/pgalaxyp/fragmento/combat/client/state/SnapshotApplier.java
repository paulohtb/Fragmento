package com.pgalaxyp.fragmento.combat.client.state;

import com.pgalaxyp.fragmento.combat.network.payload.s2c.CombatSnapshotPayload;

public final class SnapshotApplier {

    private final ClientCombatState state;

    public SnapshotApplier(ClientCombatState state) {
        this.state = state;
    }

    public void apply(CombatSnapshotPayload payload) {
        if (payload == null || payload.snapshot() == null) {
            return;
        }
        state.apply(payload.snapshot());
    }
}