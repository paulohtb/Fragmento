package com.pgalaxyp.fragmento.combat.state.snapshot;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

import java.util.Map;

public record AbilitySnapshot(
        Map<SkillId, CombatTime> cooldownEndsAt,
        Map<SkillSlotId, SkillId> infusedArmed,
        Map<SkillSlotId, CastState> casting
) {
    public record CastState(
            CombatTime castEndsAt,
            boolean ready
    ) {}
}