package com.pgalaxyp.fragmento.tier.common.service;

import com.pgalaxyp.fragmento.tier.common.model.Tier;
import java.util.Objects;

public record TierSnapshot(
        Tier tier,
        long fetchedAtMillis,
        long expiresAtMillis,
        long version
) {
    public TierSnapshot {
        Objects.requireNonNull(tier, "tier");
    }

    public boolean expired(long nowMillis) {
        return nowMillis >= expiresAtMillis;
    }
}