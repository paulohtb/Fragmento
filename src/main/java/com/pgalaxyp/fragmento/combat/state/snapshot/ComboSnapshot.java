package com.pgalaxyp.fragmento.combat.state.snapshot;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

public record ComboSnapshot(
        int stepIndex,
        CombatTime nextStepAt,
        boolean holding,
        boolean holdLatched
) {
    public static ComboSnapshot idle() {
        return new ComboSnapshot(0, CombatTime.ofTicks(0), false, false);
    }
}