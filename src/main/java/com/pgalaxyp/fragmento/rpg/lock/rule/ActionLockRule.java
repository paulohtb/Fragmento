package com.pgalaxyp.fragmento.rpg.lock.rule;

import com.pgalaxyp.fragmento.rpg.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.timing.Duration;
import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.state.runtime.ActionLockState;

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