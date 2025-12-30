package com.pgalaxyp.fragmento.tier.api;

import com.pgalaxyp.fragmento.tier.common.service.TierService;
import com.pgalaxyp.fragmento.tier.common.service.TierSnapshot;
import java.util.Objects;
import java.util.UUID;

public final class TierProgressionView implements PlayerProgressionView {

    private final TierService tiers;

    public TierProgressionView(TierService tiers) {
        this.tiers = Objects.requireNonNull(tiers);
    }

    @Override
    public int level(UUID playerId) {
        if (playerId == null) return 0;
        TierSnapshot snap = tiers.snapshot(playerId, System.currentTimeMillis());
        return snap.tier().level().value();
    }

    @Override
    public long version(UUID playerId) {
        if (playerId == null) return 0L;
        return tiers.snapshot(playerId, System.currentTimeMillis()).version();
    }

    @Override
    public String label(UUID playerId) {
        return "Tier: " + level(playerId);
    }
}