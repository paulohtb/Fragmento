package com.pgalaxyp.fragmento.rpg_old.state.runtime;

import com.pgalaxyp.fragmento.rpg_old.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;

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