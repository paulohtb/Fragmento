package com.pgalaxyp.fragmento.combat.basicAttackModule.api;

import java.util.List;
import java.util.Objects;

public record BasicAttackDefinition(List<BasicAttackStepDefinition> steps, int comboWindowFrames) {
    public BasicAttackDefinition {
        steps = List.copyOf(Objects.requireNonNull(steps));
        if (steps.isEmpty()) throw new IllegalArgumentException();
        for (var s : steps) Objects.requireNonNull(s);
        if (comboWindowFrames <= 0) throw new IllegalArgumentException();
    }
}
