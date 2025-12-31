package com.pgalaxyp.fragmento.combat.state.snapshot;

public record CombatSnapshotVersion(
        long value
) {
    public CombatSnapshotVersion {
        if (value < 0) {
            throw new IllegalArgumentException("Snapshot version must be >= 0");
        }
    }

    public CombatSnapshotVersion next() {
        return new CombatSnapshotVersion(value + 1);
    }

    public boolean isAfter(CombatSnapshotVersion other) {
        if (other == null) {
            return true;
        }
        return this.value > other.value;
    }

    public static CombatSnapshotVersion initial() {
        return new CombatSnapshotVersion(0);
    }
}