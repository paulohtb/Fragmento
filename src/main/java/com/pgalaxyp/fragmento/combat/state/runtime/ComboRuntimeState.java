package com.pgalaxyp.fragmento.combat.state.runtime;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

public record ComboRuntimeState(
        int stepIndex,
        CombatTime nextStepAt,
        boolean holding,
        boolean holdLatched
) {
    public static ComboRuntimeState idle() {
        return new ComboRuntimeState(0, CombatTime.ofTicks(0L), false, false);
    }
}