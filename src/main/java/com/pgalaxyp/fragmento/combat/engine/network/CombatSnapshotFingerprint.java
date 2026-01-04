package com.pgalaxyp.fragmento.combat.engine.network;

import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshot;

public record CombatSnapshotFingerprint(
        int comboHash,
        int abilityHash,
        int lockHash,
        int loadoutHash
) {

    public static CombatSnapshotFingerprint of(CombatSnapshot s) {
        if (s == null) {
            return new CombatSnapshotFingerprint(0, 0, 0, 0);
        }

        return new CombatSnapshotFingerprint(
                s.combo() != null ? s.combo().hashCode() : 0,
                s.abilities() != null ? s.abilities().hashCode() : 0,
                s.lock() != null ? s.lock().hashCode() : 0,
                s.loadout() != null ? s.loadout().hashCode() : 0
        );
    }
}