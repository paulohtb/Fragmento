package com.pgalaxyp.fragmento.rpg.state.snapshot;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.input.SkillSlotId;

import java.util.Map;

public record AbilitySnapshot(
        Map<SkillId, Time> cooldownEndsAt,
        Map<SkillSlotId, SkillId> infusedArmed,
        Map<SkillSlotId, CastState> casting
) {
    public record CastState(
            Time castEndsAt,
            boolean ready
    ) {}
}