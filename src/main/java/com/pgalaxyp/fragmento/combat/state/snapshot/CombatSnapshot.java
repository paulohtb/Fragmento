package com.pgalaxyp.fragmento.combat.state.snapshot;

public record CombatSnapshot(
        CombatSnapshotVersion version,
        int comboStepIndex,
        boolean awaitingHit,
        int armedInfusionSkillId
) {
    public static CombatSnapshot empty() {
        return new CombatSnapshot(CombatSnapshotVersion.zero(), 0, false, 0);
    }

    public CombatSnapshot withNextVersion() {
        return new CombatSnapshot(version.next(), comboStepIndex, awaitingHit, armedInfusionSkillId);
    }
}