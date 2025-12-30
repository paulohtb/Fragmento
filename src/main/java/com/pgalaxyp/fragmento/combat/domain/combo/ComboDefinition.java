package com.pgalaxyp.fragmento.combat.domain.combo;

import java.util.List;

public record ComboDefinition(String id, List<ComboStep> steps) {

    public ComboDefinition(String id, List<ComboStep> steps) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Combo id vazio");
        }
        if (steps == null || steps.isEmpty()) {
            throw new IllegalArgumentException("Combo sem steps");
        }
        this.id = id;
        this.steps = List.copyOf(steps);
    }

    public int size() {
        return steps.size();
    }

    public ComboStep step(int index) {
        int i = Math.max(0, Math.min(index, steps.size() - 1));
        return steps.get(i);
    }
}