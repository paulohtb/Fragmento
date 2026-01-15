package com.pgalaxyp.fragmento.combat.targeting.api;

public record TargetingSpec(TargetingMode mode, int rangeBlocks, TargetingFallback fallbackPolicy) {

    public TargetingSpec {
        if (mode == null || fallbackPolicy == null) {
            throw new IllegalArgumentException();
        }
        if (rangeBlocks <= 0) {
            throw new IllegalArgumentException();
        }
    }
}