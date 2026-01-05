package com.pgalaxyp.fragmento.rpg_old.state.snapshot;

import com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg_old.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;

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