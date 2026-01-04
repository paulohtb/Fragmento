package com.pgalaxyp.fragmento.rpg.combat.rule;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.domain.timing.Duration;
import com.pgalaxyp.fragmento.rpg.state.runtime.ComboState;

public final class ComboTimingRule {

    public ComboState scheduleNext(
            ComboState state,
            Duration stepDuration,
            Time now
    ) {
        if (state == null || stepDuration == null || now == null) {
            return ComboState.idle();
        }

        return new ComboState(
                state.stepIndex(),
                now.plus(stepDuration),
                state.holding(),
                state.holdLatched()
        );
    }
}