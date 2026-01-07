package com.pgalaxyp.fragmento.rpg.core.domain.combo;

import java.util.List;

public record ComboSequence(List<ComboStepDef> steps) {
    public ComboSequence {
        steps = List.copyOf(steps);
    }

    public int size() {
        return steps.size();
    }

    public ComboStepDef step(int index) {
        return steps.get(index);
    }
}