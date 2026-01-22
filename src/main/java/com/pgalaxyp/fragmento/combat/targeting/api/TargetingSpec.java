package com.pgalaxyp.fragmento.combat.targeting.api;

import java.util.Objects;

public record TargetingSpec(TargetingMode mode, double rangeBlocks, TargetingFallback fallbackPolicy) {
    public TargetingSpec {
        Objects.requireNonNull(mode);
        Objects.requireNonNull(fallbackPolicy);
        if (!Double.isFinite(rangeBlocks) || rangeBlocks <= 0.0) throw new IllegalArgumentException();
    }
}