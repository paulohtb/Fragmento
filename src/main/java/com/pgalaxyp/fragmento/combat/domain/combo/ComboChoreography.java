package com.pgalaxyp.fragmento.combat.domain.combo;

import java.util.List;

public record ComboChoreography(
        List<ComboStep> steps
) {
    public ComboChoreography {
        if (steps == null || steps.isEmpty()) {
            throw new IllegalArgumentException("Combo must have at least one step");
        }
        steps = List.copyOf(steps);
    }

    public int size() {
        return steps.size();
    }

    public ComboStep step(int index) {
        return steps.get(index);
    }
}