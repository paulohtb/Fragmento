package com.pgalaxyp.fragmento.rpg.core.domain.combo;

import java.util.List;

public record ComboSequence(List<ComboStep> steps) {
    public ComboSequence {
        steps = List.copyOf(steps);
        if (steps.isEmpty()) throw new IllegalArgumentException();
    }

    public ComboStep stepAt(int index) {
        return steps.get(index % steps.size());
    }

    public int size() {
        return steps.size();
    }
}