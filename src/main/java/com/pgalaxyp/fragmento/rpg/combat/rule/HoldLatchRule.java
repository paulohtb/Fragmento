package com.pgalaxyp.fragmento.rpg.combat.rule;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.state.runtime.ComboState;

public final class HoldLatchRule {

    public ComboState advanceIfLatched(
            ComboState state,
            Time now
    ) {
        if (state == null || now == null) {
            return ComboState.idle();
        }
        if (!state.holdLatched()) {
            return state;
        }
        if (now.isBefore(state.nextStepAt())) {
            return state;
        }
        return new ComboState(
                state.stepIndex() + 1,
                now,
                state.holding(),
                state.holding()
        );
    }
}