package com.pgalaxyp.fragmento.rpg.gameplay.combat.basic;

import java.util.List;

public record BasicSequence(List<ComboStep> steps) {
    public BasicSequence {
        steps = List.copyOf(steps);
        if (steps.isEmpty()) throw new IllegalArgumentException("steps empty");
    }
}