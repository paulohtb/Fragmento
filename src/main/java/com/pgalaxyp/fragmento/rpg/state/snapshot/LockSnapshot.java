package com.pgalaxyp.fragmento.rpg.state.snapshot;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;

public record LockSnapshot(
        ActionKind actionKind,
        SkillId skillId,
        Time actionEndsAt,
        Time itemSwapLockedUntil
) {}