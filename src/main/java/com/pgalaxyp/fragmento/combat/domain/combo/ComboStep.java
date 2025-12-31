package com.pgalaxyp.fragmento.combat.domain.combo;

public record ComboStep(
        int index
) {
    public ComboStep {
        if (index < 0) {
            throw new IllegalArgumentException("ComboStep index must be >= 0");
        }
    }
}