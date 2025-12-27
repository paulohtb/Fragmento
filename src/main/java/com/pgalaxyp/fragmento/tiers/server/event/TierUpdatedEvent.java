package com.pgalaxyp.fragmento.tiers.server.event;

import com.pgalaxyp.fragmento.tiers.api.Tier;
import java.util.UUID;

public record TierUpdatedEvent(
        UUID playerId,
        Tier tier,
        long version
) {}