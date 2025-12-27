package com.pgalaxyp.fragmento.tiers.server.service;

import com.pgalaxyp.fragmento.tiers.api.Tier;
import com.pgalaxyp.fragmento.tiers.api.TierLevel;
import com.pgalaxyp.fragmento.tiers.api.TierStatus;
import com.pgalaxyp.fragmento.tiers.server.event.TierUpdatedEvent;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public final class NoopTierService implements TierService {

    @Override
    public TierSnapshot snapshot(UUID playerId, long nowMillis) {
        Objects.requireNonNull(playerId, "playerId");
        Tier tier = new Tier(TierLevel.TIER_0, TierStatus.ACTIVE);
        return new TierSnapshot(tier, nowMillis, nowMillis, 0L);
    }

    @Override
    public void registerListener(Consumer<TierUpdatedEvent> listener) {
        Objects.requireNonNull(listener, "listener");
    }
}