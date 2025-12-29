package com.pgalaxyp.fragmento.tiers.common.model;

import java.util.Objects;

public record Tier(TierLevel level) {

    public Tier {
        Objects.requireNonNull(level, "level");
    }

    public static Tier inactive() {
        return new Tier(TierLevel.TIER_0);
    }

    public boolean allows(TierLevel required) {
        return level.isAtLeast(required);
    }
}