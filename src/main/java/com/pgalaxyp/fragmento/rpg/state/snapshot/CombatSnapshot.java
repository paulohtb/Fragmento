package com.pgalaxyp.fragmento.rpg.state.snapshot;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;

public record CombatSnapshot(
        CombatSnapshotVersion version,
        ComboSnapshot combo,
        AbilitySnapshot abilities,
        ExecutionSnapshot execution,
        LockSnapshot lock,
        LoadoutSnapshot loadout
) {

    public Time now() {
        if (combo != null && combo.nextStepAt() != null) {
            return combo.nextStepAt();
        }
        return Time.ofTicks(0L);
    }
}