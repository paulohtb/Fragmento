package com.pgalaxyp.fragmento.combat.state.snapshot;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

import java.util.Map;

public record SkillSnapshot(
        Map<SkillId, CombatTime> cooldownEndsAt
) {
    public static SkillSnapshot empty() {
        return new SkillSnapshot(Map.of());
    }
}