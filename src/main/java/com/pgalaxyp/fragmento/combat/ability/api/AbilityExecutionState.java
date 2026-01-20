package com.pgalaxyp.fragmento.combat.ability.api;

import java.util.Objects;

public record AbilityExecutionState(AbilityExecutionPhase phase, AbilitySnapshot ability) {
    public AbilityExecutionState {
        Objects.requireNonNull(phase);
        if (phase == AbilityExecutionPhase.IDLE && ability != null) throw new IllegalArgumentException();
        if (phase != AbilityExecutionPhase.IDLE && ability == null) throw new IllegalArgumentException();
    }
}