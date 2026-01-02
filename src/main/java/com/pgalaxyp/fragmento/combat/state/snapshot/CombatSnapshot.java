package com.pgalaxyp.fragmento.combat.state.snapshot;

public record CombatSnapshot(
        CombatSnapshotVersion version,
        ComboSnapshot weapon,
        SkillSnapshot skills
) {}