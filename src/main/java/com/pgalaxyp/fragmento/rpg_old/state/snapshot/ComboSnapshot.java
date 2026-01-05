package com.pgalaxyp.fragmento.rpg_old.state.snapshot;

import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;

public record ComboSnapshot(
        int stepIndex,
        Time nextStepAt,
        boolean holding,
        boolean holdLatched
) {}