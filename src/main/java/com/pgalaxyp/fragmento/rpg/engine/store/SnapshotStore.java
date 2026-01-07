package com.pgalaxyp.fragmento.rpg.engine.store;

public final class SnapshotStore {

    private long version;

    public void nextSnapshot(CombatStateStore state) {
        version++;
    }
}