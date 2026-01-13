package com.pgalaxyp.fragmento.rpg.targeting.api;

import java.util.Optional;

public record TargetResult(
        Target target,
        Optional<TargetingFallbackPolicy> appliedFallback
) {
    public TargetResult {
        if (target == null || appliedFallback == null) {
            throw new IllegalArgumentException();
        }
        if (appliedFallback.isPresent() && appliedFallback.get() == null) {
            throw new IllegalArgumentException();
        }
        appliedFallback = appliedFallback.isPresent() ? Optional.of(appliedFallback.get()) : Optional.empty();
    }

    public boolean usedFallback() {
        return appliedFallback.isPresent();
    }

    public static TargetResult direct(Target target) {
        if (target == null) {
            throw new IllegalArgumentException();
        }
        return new TargetResult(target, Optional.empty());
    }

    public static TargetResult fallback(Target target, TargetingFallbackPolicy policy) {
        if (target == null || policy == null) {
            throw new IllegalArgumentException();
        }
        return new TargetResult(target, Optional.of(policy));
    }
}