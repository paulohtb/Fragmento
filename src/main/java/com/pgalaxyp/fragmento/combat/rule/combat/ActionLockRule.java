package com.pgalaxyp.fragmento.combat.rule.combat;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.domain.timing.Duration;
import com.pgalaxyp.fragmento.combat.state.runtime.ActionLockState;

public final class ActionLockRule {

    public ActionLockState lock(
            Duration duration,
            CombatTime now
    ) {
        if (duration == null || now == null) {
            return ActionLockState.idle();
        }
        CombatTime endsAt = now.plus(duration);
        return new ActionLockState(
                "",
                endsAt,
                endsAt
        );
    }
}