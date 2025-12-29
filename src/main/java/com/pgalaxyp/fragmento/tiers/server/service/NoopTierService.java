package com.pgalaxyp.fragmento.tiers.server.service;

import com.pgalaxyp.fragmento.tiers.common.model.Tier;
import com.pgalaxyp.fragmento.tiers.common.service.TierService;
import com.pgalaxyp.fragmento.tiers.common.service.TierSnapshot;
import com.pgalaxyp.fragmento.tiers.common.service.TierUpdatedEvent;
import java.util.UUID;
import java.util.function.Consumer;

public final class NoopTierService implements TierService {

    static final NoopTierService INSTANCE = new NoopTierService();

    private NoopTierService() {}

    @Override
    public TierSnapshot snapshot(UUID playerId, long nowMillis) {
        return new TierSnapshot(Tier.inactive(), nowMillis, nowMillis, 0L);
    }

    @Override
    public void registerListener(Consumer<TierUpdatedEvent> listener) {
    }
}