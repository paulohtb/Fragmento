package com.pgalaxyp.fragmento.rpg_old.state.snapshot;

import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;

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