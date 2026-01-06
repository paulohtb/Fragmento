package com.pgalaxyp.fragmento.rpg.gameplay.combat.basic;

import java.util.List;
import java.util.Objects;

public record BasicSequence(List<ComboStep> steps) {
    public BasicSequence {
        steps = List.copyOf(steps);
        if (steps.isEmpty()) throw new IllegalArgumentException("steps empty");
    }

    public ComboStep step(String stepId) {
        for (var s : steps) {
            if (Objects.equals(s.stepId(), stepId)) return s;
        }
        throw new IllegalStateException("Missing stepId: " + stepId);
    }
}