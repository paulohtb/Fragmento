package com.pgalaxyp.fragmento.rpg_old.state.snapshot;

import com.pgalaxyp.fragmento.rpg_old.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;

public record LockSnapshot(
        ActionKind actionKind,
        SkillId skillId,
        Time actionEndsAt,
        Time itemSwapLockedUntil
) {}