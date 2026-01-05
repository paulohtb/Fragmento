package com.pgalaxyp.fragmento.rpg.domain.execution;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;

public record ExecutionFacts(
        Time now,
        boolean hasActiveCombatEntity
) {
    public ExecutionFacts {
        if (now == null) {
            throw new IllegalArgumentException();
        }
    }
}