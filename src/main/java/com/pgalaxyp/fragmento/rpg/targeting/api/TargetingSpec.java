package com.pgalaxyp.fragmento.rpg.targeting.api;

public record TargetingSpec(
        TargetingMode mode,
        int rangeBlocks,
        TargetingFallbackPolicy fallbackPolicy
) {
    public TargetingSpec {
        if (mode == null || fallbackPolicy == null) {
            throw new IllegalArgumentException();
        }
        if (rangeBlocks <= 0) {
            throw new IllegalArgumentException();
        }
    }
}