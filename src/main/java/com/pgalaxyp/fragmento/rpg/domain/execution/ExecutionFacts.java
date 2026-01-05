package com.pgalaxyp.fragmento.rpg.domain.execution;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;

public record ExecutionFacts(
        Time now,
        boolean onGround,
        boolean inWater
) {
    public boolean hasActiveCombatEntity() {
        return onGround || inWater;
    }
}