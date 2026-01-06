package com.pgalaxyp.fragmento.rpg.engine.store;

import com.pgalaxyp.fragmento.rpg.core.state.snapshot.CombatSnapshot;

public final class SnapshotStore {

    private long version;

    public CombatSnapshot nextSnapshot(CombatStateStore state) {
        version++;
        return new CombatSnapshot(
                version,
                state.copyActors()
        );
    }
}