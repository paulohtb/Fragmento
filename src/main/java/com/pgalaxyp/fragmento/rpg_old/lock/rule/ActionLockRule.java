package com.pgalaxyp.fragmento.rpg_old.lock.rule;

import com.pgalaxyp.fragmento.rpg_old.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg_old.domain.timing.Duration;
import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg_old.state.runtime.ActionLockState;

public final class ActionLockRule {

    public ActionLockState lock(
            ActionKind kind,
            SkillId skillId,
            Duration duration,
            Time now
    ) {
        if (now == null) {
            return ActionLockState.idle();
        }

        ActionKind safeKind = kind != null ? kind : ActionKind.NONE;
        Duration safeDuration = duration != null ? duration : Duration.ofTicks(0L);

        Time endsAt = now.plus(safeDuration);

        Time swapLockUntil = Time.ZERO;
        if (safeKind == ActionKind.CAST_FINISH) {
            swapLockUntil = endsAt;
        }

        return new ActionLockState(
                true,
                safeKind,
                skillId,
                endsAt,
                swapLockUntil
        );
    }
}