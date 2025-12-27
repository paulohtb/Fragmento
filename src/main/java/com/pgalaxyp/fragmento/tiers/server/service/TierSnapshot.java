package com.pgalaxyp.fragmento.tiers.server.service;

import com.pgalaxyp.fragmento.tiers.api.Tier;
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