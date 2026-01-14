package com.pgalaxyp.fragmento.rpg.combo.model;

import java.util.*;

public record ComboPattern(List<ComboStep> steps) {

    public ComboPattern {
        if (steps == null || steps.isEmpty()) throw new IllegalArgumentException();
        steps = List.copyOf(steps);

        for (int i = 0; i < steps.size(); i++) {
            ComboStep comboStep = steps.get(i);
            if (comboStep == null || comboStep.index() != i) throw new IllegalArgumentException();
        }
    }

    public int size() { return steps.size(); }

    public ComboStep step(int index) { return steps.get(index); }
}