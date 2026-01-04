package com.pgalaxyp.fragmento.rpg.lock.rule;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntentKind;
import com.pgalaxyp.fragmento.rpg.state.runtime.ActionLockState;

public final class LockGateRule {

    public boolean allowAttack(ActionLockState lock, Time now) {
        if (lock == null || now == null) {
            return true;
        }
        return !lock.isActive(now);
    }

    public boolean allowAbility(ActionLockState lock, AbilityIntent intent, Time now) {
        if (lock == null || intent == null || now == null) {
            return true;
        }

        if (!lock.isActive(now)) {
            return true;
        }

        AbilityIntentKind k = intent.kind();
        return k == AbilityIntentKind.CANCEL || k == AbilityIntentKind.TOGGLE;
    }
}