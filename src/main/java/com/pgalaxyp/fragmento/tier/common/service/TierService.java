package com.pgalaxyp.fragmento.tier.common.service;

import com.pgalaxyp.fragmento.tier.common.model.Tier;
import com.pgalaxyp.fragmento.tier.common.model.TierLevel;
import java.util.UUID;
import java.util.function.Consumer;

public interface TierService {

    TierSnapshot snapshot(UUID playerId, long nowMillis);

    void registerListener(Consumer<TierUpdatedEvent> listener);

    void invalidate(UUID playerId);

    void applyLocal(UUID playerId, Tier tier, long nowMillis);

    default void applyRedeem(UUID playerId, int level, long nowMillis) {
        if (level <= 0) {
            return;
        }
        applyLocal(playerId, new Tier(TierLevel.of(level)), nowMillis);
    }
}