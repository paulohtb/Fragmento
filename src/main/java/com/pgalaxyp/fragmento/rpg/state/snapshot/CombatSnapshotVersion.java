package com.pgalaxyp.fragmento.rpg.state.snapshot;

public record CombatSnapshotVersion(long value) {

    public static CombatSnapshotVersion initial() {
        return new CombatSnapshotVersion(0L);
    }

    public CombatSnapshotVersion next() {
        return new CombatSnapshotVersion(value + 1L);
    }

    public long raw() {
        return value;
    }

    public boolean isAfter(CombatSnapshotVersion other) {
        if (other == null) return true;
        return value > other.value;
    }
}