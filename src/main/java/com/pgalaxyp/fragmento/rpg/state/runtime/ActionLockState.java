package com.pgalaxyp.fragmento.rpg.state.runtime;

import com.pgalaxyp.fragmento.rpg.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.timing.Time;

public record ActionLockState(
        boolean active,
        ActionKind actionKind,
        SkillId skillId,
        Time endsAt,
        Time itemSwapLockedUntil
) {

    public static ActionLockState idle() {
        return new ActionLockState(false, ActionKind.NONE, null, Time.ZERO, Time.ZERO);
    }

    public boolean isActive(Time now) {
        if (!active) return false;
        if (now == null) return false;
        if (endsAt == null) return true;
        return now.ticks() < endsAt.ticks();
    }
}