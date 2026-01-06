package com.pgalaxyp.fragmento.rpg.core.domain.combo;

import java.util.List;

public record ComboSequence(List<ComboStepDef> steps) {
    public ComboSequence {
        steps = List.copyOf(steps);
    }
}