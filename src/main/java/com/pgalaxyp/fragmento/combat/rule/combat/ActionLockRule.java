package com.pgalaxyp.fragmento.combat.rule.combat;

import com.pgalaxyp.fragmento.combat.domain.action.ActionKind;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.domain.timing.Duration;
import com.pgalaxyp.fragmento.combat.state.runtime.ActionLockState;

public final class ActionLockRule {

    public ActionLockState lock(
            ActionKind kind,
            Duration duration,
            CombatTime now
    ) {
        CombatTime endsAt = now.plus(duration);
        return new ActionLockState(kind, endsAt, endsAt);
    }
}