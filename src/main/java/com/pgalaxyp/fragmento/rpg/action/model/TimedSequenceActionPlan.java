package com.pgalaxyp.fragmento.rpg.action.model;

import java.util.*;

public record TimedSequenceActionPlan(List<EffectStep> steps, int windowFrames) implements ActionPlan {

    public TimedSequenceActionPlan {
        Objects.requireNonNull(steps);
        if (steps.isEmpty()) throw new IllegalArgumentException();
        if (windowFrames <= 0) throw new IllegalArgumentException();
        steps = List.copyOf(steps);
    }
}