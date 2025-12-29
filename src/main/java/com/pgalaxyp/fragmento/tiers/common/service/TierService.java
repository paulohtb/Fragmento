package com.pgalaxyp.fragmento.tiers.common.service;

import com.pgalaxyp.fragmento.tiers.common.model.Tier;

import java.util.UUID;
import java.util.function.Consumer;

public interface TierService {

    TierSnapshot snapshot(UUID playerId, long nowMillis);

    void registerListener(Consumer<TierUpdatedEvent> listener);

    default void invalidate(UUID playerId) {}

    default void applyLocal(UUID playerId, Tier tier, long nowMillis) {}
}