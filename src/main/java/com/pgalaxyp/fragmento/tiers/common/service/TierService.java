package com.pgalaxyp.fragmento.tiers.common.service;

import com.pgalaxyp.fragmento.tiers.common.model.Tier;
import com.pgalaxyp.fragmento.tiers.common.model.TierLevel;
import java.util.UUID;
import java.util.function.Consumer;

public interface TierService {

    TierSnapshot snapshot(UUID playerId, long nowMillis);

    void registerListener(Consumer<TierUpdatedEvent> listener);

    default void invalidate(UUID playerId) {}

    default void applyLocal(UUID playerId, Tier tier, long nowMillis) {}

    default boolean applyRedeem(UUID playerId, int level, long nowMillis) {
        if (playerId == null) {
            return false;
        }
        if (level <= 0) {
            return false;
        }
        Tier tier = new Tier(TierLevel.of(level));
        applyLocal(playerId, tier, nowMillis);
        return true;
    }
}