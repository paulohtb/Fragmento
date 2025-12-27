package com.pgalaxyp.fragmento.tiers.api;

import java.util.Objects;

public record Tier(TierLevel level, TierStatus status) {

    public Tier {
        Objects.requireNonNull(level, "level");
        Objects.requireNonNull(status, "status");
    }

    public static Tier unknown() {
        return new Tier(TierLevel.TIER_0, TierStatus.UNKNOWN);
    }

    public static Tier inactive() {
        return new Tier(TierLevel.TIER_0, TierStatus.INACTIVE);
    }

    public static Tier error() {
        return new Tier(TierLevel.TIER_0, TierStatus.ERROR);
    }

    public boolean active() {
        return status == TierStatus.ACTIVE;
    }

    public boolean allows(TierLevel required) {
        if (required == null) return true;
        return level.isAtLeast(required);
    }
}