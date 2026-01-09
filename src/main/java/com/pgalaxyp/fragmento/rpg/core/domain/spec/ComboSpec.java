package com.pgalaxyp.fragmento.rpg.core.domain.spec;

public record ComboSpec(
        int minStepsTotal,
        int maxStepsTotal
) {
    public ComboSpec {
        if (minStepsTotal <= 0 || maxStepsTotal <= 0 || minStepsTotal > maxStepsTotal) {
            throw new IllegalArgumentException();
        }
    }
}