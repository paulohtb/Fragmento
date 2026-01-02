package com.pgalaxyp.fragmento.combat.rule.combat;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.state.runtime.ComboRuntimeState;

public final class HoldLatchRule {

    public ComboRuntimeState advanceIfLatched(
            ComboRuntimeState state,
            CombatTime now
    ) {
        if (state == null || now == null) {
            return ComboRuntimeState.idle();
        }
        if (!state.holdLatched()) {
            return state;
        }
        if (now.isBefore(state.nextStepAt())) {
            return state;
        }
        return new ComboRuntimeState(
                state.stepIndex() + 1,
                now,
                state.holding(),
                state.holding()
        );
    }
}