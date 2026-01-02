package com.pgalaxyp.fragmento.combat.rule.combat;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.domain.timing.Duration;
import com.pgalaxyp.fragmento.combat.state.runtime.ComboRuntimeState;

public final class ComboTimingRule {

    public ComboRuntimeState scheduleNext(
            ComboRuntimeState state,
            Duration stepDuration,
            CombatTime now
    ) {
        if (state == null || stepDuration == null || now == null) {
            return ComboRuntimeState.idle();
        }

        return new ComboRuntimeState(
                state.stepIndex(),
                now.plus(stepDuration),
                state.holding(),
                state.holdLatched()
        );
    }
}