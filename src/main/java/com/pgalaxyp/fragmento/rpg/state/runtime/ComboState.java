package com.pgalaxyp.fragmento.rpg.state.runtime;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;

public record ComboState(
        int stepIndex,
        Time nextStepAt,
        boolean holding,
        boolean holdLatched
) {

    public static ComboState initial() {
        return new ComboState(
                0,
                Time.ofTicks(0L),
                false,
                false
        );
    }

    public static ComboState idle() {
        return initial();
    }
}