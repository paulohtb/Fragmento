package com.pgalaxyp.fragmento.rpg.state.snapshot;

import com.pgalaxyp.fragmento.rpg.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.timing.Time;

public record LockSnapshot(
        ActionKind actionKind,
        SkillId skillId,
        Time actionEndsAt,
        Time itemSwapLockedUntil
) {}