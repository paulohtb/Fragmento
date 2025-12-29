package com.pgalaxyp.fragmento.tiers.common.service;

import com.pgalaxyp.fragmento.tiers.common.model.Tier;
import java.util.Objects;
import java.util.UUID;

public record TierUpdatedEvent(
        UUID playerId,
        Tier tier,
        long version
) {
    public TierUpdatedEvent {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(tier, "tier");
    }
}