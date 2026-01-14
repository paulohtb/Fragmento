package com.pgalaxyp.fragmento.rpg.action.type;

import java.util.*;

public record ComboActionPlan(List<ComboActionStep> steps, int stepWindowFrames) implements ActionPlan {
    public ComboActionPlan {
        if (steps == null) {
            throw new IllegalArgumentException();
        }
        if (stepWindowFrames <= 0) {
            throw new IllegalArgumentException();
        }

        steps = List.copyOf(steps);
        if (steps.isEmpty()) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < steps.size(); i++) {
            ComboActionStep s = steps.get(i);
            if (s == null) {
                throw new IllegalArgumentException();
            }
            if (s.index() != i) {
                throw new IllegalArgumentException();
            }
        }
    }

    public int stepsTotal() {
        return steps.size();
    }

    public ComboActionStep step(int index) {
        return steps.get(index);
    }
}