package com.pgalaxyp.fragmento.rpg.state.snapshot;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;

public record CombatSnapshot(
        CombatSnapshotVersion version,
        Time now,
        ComboSnapshot combo,
        AbilitySnapshot abilities,
        ExecutionSnapshot execution,
        LockSnapshot lock,
        LoadoutSnapshot loadout,
        EquippedSkillsSnapshot equippedSkills
) {}