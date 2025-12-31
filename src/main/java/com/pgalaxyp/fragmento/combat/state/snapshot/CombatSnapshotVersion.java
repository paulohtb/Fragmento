package com.pgalaxyp.fragmento.combat.state.snapshot;

public record CombatSnapshotVersion(long value) {
    public static CombatSnapshotVersion zero() {
        return new CombatSnapshotVersion(0L);
    }

    public CombatSnapshotVersion next() {
        return new CombatSnapshotVersion(value + 1L);
    }

    public boolean isAfter(CombatSnapshotVersion other) {
        return other != null && value > other.value;
    }
}