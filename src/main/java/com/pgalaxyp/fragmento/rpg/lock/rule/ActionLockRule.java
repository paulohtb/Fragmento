package com.pgalaxyp.fragmento.rpg.lock.rule;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.timing.Duration;
import com.pgalaxyp.fragmento.rpg.state.runtime.ActionLockState;

public final class ActionLockRule {

    public ActionLockState lock(ActionKind kind, SkillId skillId, Duration duration, Time now) {
        ActionKind safeKind = kind != null ? kind : ActionKind.NONE;
        Time endsAt = now.plus(duration != null ? duration : Duration.ofTicks(0L));

        Time swapLockUntil = Time.ofTicks(0L);
        if (safeKind == ActionKind.CAST_FINISH) {
            swapLockUntil = endsAt;
        }

        return new ActionLockState(safeKind, skillId, endsAt, swapLockUntil);
    }
}