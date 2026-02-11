package com.pgalaxyp.fragmento.combat.targetingModule.api;

import java.util.Objects;

public record TargetingSpec(TargetingMode mode, double minRangeBlocks, double maxRangeBlocks, double fallbackDistanceBlocks, TargetingFallback fallbackPolicy) {
    public TargetingSpec {
        Objects.requireNonNull(mode);
        Objects.requireNonNull(fallbackPolicy);

        if (!Double.isFinite(minRangeBlocks) || minRangeBlocks < 0.0) throw new IllegalArgumentException();
        if (!Double.isFinite(maxRangeBlocks) || maxRangeBlocks <= minRangeBlocks) throw new IllegalArgumentException();
        if (!Double.isFinite(fallbackDistanceBlocks) || fallbackDistanceBlocks < 0.0) throw new IllegalArgumentException();
    }
}
