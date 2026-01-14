package com.pgalaxyp.fragmento.rpg.combo.model;

import java.util.*;

public record ComboPattern(List<ComboStep> steps) {

    public ComboPattern {
        if (steps == null) {
            throw new IllegalArgumentException();
        }
        if (steps.isEmpty()) {
            throw new IllegalArgumentException();
        }

        steps = List.copyOf(steps);
        int size = steps.size();
        for (int i = 0; i < size; i++) {
            ComboStep s = steps.get(i);
            if (s == null) {
                throw new IllegalArgumentException();
            }
            if (s.index() != i) {
                throw new IllegalArgumentException();
            }
        }
    }

    public int size() {
        return steps.size();
    }

    public ComboStep step(int index) {
        return steps.get(index);
    }
}