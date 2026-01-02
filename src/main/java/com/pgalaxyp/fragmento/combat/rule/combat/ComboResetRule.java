package com.pgalaxyp.fragmento.combat.rule.combat;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.state.runtime.ComboRuntimeState;

public final class ComboResetRule {

    public ComboRuntimeState resetIfFinished(
            ComboRuntimeState state,
            int maxSteps,
            CombatTime now
    ) {
        if (state == null || now == null) {
            return ComboRuntimeState.idle();
        }

        if (maxSteps <= 0) {
            return ComboRuntimeState.idle();
        }

        if (state.stepIndex() < maxSteps) {
            return state;
        }

        if (state.holding()) {
            return new ComboRuntimeState(
                    0,
                    now,
                    true,
                    true
            );
        }

        return ComboRuntimeState.idle();
    }
}