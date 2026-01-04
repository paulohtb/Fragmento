package com.pgalaxyp.fragmento.rpg.combat.rule;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.state.runtime.ComboState;

public final class ComboResetRule {

    public ComboState resetIfFinished(
            ComboState state,
            int maxSteps,
            Time now
    ) {
        if (state == null || now == null) {
            return ComboState.idle();
        }

        if (maxSteps <= 0) {
            return ComboState.idle();
        }

        if (state.stepIndex() < maxSteps) {
            return state;
        }

        if (state.holding()) {
            return new ComboState(
                    0,
                    now,
                    true,
                    true
            );
        }

        return ComboState.idle();
    }
}