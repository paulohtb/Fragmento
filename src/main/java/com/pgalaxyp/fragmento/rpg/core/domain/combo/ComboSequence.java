package com.pgalaxyp.fragmento.rpg.core.domain.combo;

import java.util.List;

public record ComboSequence(
        List<ComboStepDef> steps
) {
    public ComboSequence {
        steps = steps == null ? List.of() : List.copyOf(steps);
        if (steps.isEmpty()) throw new IllegalArgumentException("ComboSequence.steps");
    }
}