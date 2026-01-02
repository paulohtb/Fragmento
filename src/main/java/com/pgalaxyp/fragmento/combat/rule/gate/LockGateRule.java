package com.pgalaxyp.fragmento.combat.rule.gate;

import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntentKind;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.state.runtime.ActionLockState;

public final class LockGateRule {

    public boolean allowAttack(
            ActionLockState lock,
            CombatTime now
    ) {
        if (lock == null || now == null) {
            return true;
        }
        return !lock.active(now);
    }

    public boolean allowAbility(
            ActionLockState lock,
            AbilityIntent intent,
            CombatTime now
    ) {
        if (lock == null || intent == null || now == null) {
            return true;
        }

        if (!lock.active(now)) {
            return true;
        }

        return intent.kind() == AbilityIntentKind.CANCEL;
    }
}