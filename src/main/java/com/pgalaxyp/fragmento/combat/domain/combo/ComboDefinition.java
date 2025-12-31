package com.pgalaxyp.fragmento.combat.domain.combo;

import java.util.List;

public record ComboDefinition(
        List<ComboStep> steps
) {
    public ComboDefinition {
        if (steps == null || steps.isEmpty()) {
            throw new IllegalArgumentException("Combo must have at least one step");
        }
    }

    public int size() {
        return steps.size();
    }

    public ComboStep step(int index) {
        if (index < 0 || index >= steps.size()) {
            return steps.getFirst();
        }
        return steps.get(index);
    }
}