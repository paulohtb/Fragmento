package com.pgalaxyp.fragmento.combat.combo.model;

import java.util.*;

public record ComboPattern(List<ComboStep> steps) {

    public ComboPattern {
        if (steps == null || steps.isEmpty()) throw new IllegalArgumentException();
        steps = List.copyOf(steps);
        for (int i = 0; i < steps.size(); i++) {
            ComboStep step = steps.get(i);
            if (step == null || step.index() != i) throw new IllegalArgumentException();
        }
    }

    public int size() { return steps.size(); }

    public ComboStep step(int index) { return steps.get(index); }
}