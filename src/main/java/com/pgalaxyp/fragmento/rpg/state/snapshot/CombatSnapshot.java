package com.pgalaxyp.fragmento.rpg.state.snapshot;

public record CombatSnapshot(
        CombatSnapshotVersion version,
        ComboSnapshot combo,
        AbilitySnapshot abilities,
        LockSnapshot lock,
        LoadoutSnapshot loadout
) {}