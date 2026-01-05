package com.pgalaxyp.fragmento.rpg_old.combat.rule;

import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg_old.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.rpg_old.state.runtime.ComboState;

public final class ComboRule {

    public ComboState apply(
            ComboState state,
            AttackIntent intent,
            Time now
    ) {
        if (state == null || intent == null || now == null) {
            return ComboState.idle();
        }

        if (intent == AttackIntent.CLICK) {
            return new ComboState(
                    state.stepIndex() + 1,
                    now,
                    false,
                    false
            );
        }

        if (intent == AttackIntent.HOLD_START) {
            return new ComboState(
                    state.stepIndex(),
                    state.nextStepAt(),
                    true,
                    true
            );
        }

        if (intent == AttackIntent.HOLD_STOP) {
            return new ComboState(
                    state.stepIndex(),
                    state.nextStepAt(),
                    false,
                    false
            );
        }

        return state;
    }
}