package com.pgalaxyp.fragmento.rpg.state.snapshot;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;

public record ComboSnapshot(
        int stepIndex,
        Time nextStepAt,
        boolean holding,
        boolean holdLatched
) {
    public static ComboSnapshot idle() {
        return new ComboSnapshot(0, Time.ofTicks(0), false, false);
    }
}