package com.pgalaxyp.fragmento.cosmetics.server.validation;

import com.pgalaxyp.fragmento.tiers.api.Tier;
import com.pgalaxyp.fragmento.tiers.server.service.TierService;
import java.util.Objects;
import java.util.UUID;

public final class TierServicePlayerTierResolver implements PlayerTierResolver {

    private final TierService tierService;

    public TierServicePlayerTierResolver(TierService tierService) {
        this.tierService = Objects.requireNonNull(tierService, "tierService");
    }

    @Override
    public Tier resolve(UUID playerId) {
        if (playerId == null) return null;
        return tierService.snapshot(playerId, System.currentTimeMillis()).tier();
    }
}