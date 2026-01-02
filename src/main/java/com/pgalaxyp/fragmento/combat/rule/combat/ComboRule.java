package com.pgalaxyp.fragmento.combat.rule.combat;

import com.pgalaxyp.fragmento.combat.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.state.runtime.ComboRuntimeState;

public final class ComboRule {

    public ComboRuntimeState apply(
            ComboRuntimeState state,
            AttackIntent intent,
            CombatTime now
    ) {
        if (state == null || intent == null || now == null) {
            return ComboRuntimeState.idle();
        }

        if (intent == AttackIntent.CLICK) {
            return new ComboRuntimeState(
                    state.stepIndex() + 1,
                    now,
                    false,
                    false
            );
        }

        if (intent == AttackIntent.HOLD_START) {
            boolean latch = state.holdLatched() || now.isBefore(state.nextStepAt());
            return new ComboRuntimeState(
                    state.stepIndex(),
                    state.nextStepAt(),
                    true,
                    latch
            );
        }

        if (intent == AttackIntent.HOLD_STOP) {
            return new ComboRuntimeState(
                    state.stepIndex(),
                    state.nextStepAt(),
                    false,
                    false
            );
        }

        return state;
    }
}