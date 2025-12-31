package com.pgalaxyp.fragmento.combat.state.snapshot;

public record CombatSnapshot(
        CombatSnapshotVersion version,
        WeaponSnapshot weapon,
        SkillSnapshot skills
) {}