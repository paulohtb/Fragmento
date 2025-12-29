package com.pgalaxyp.fragmento.cosmetics.common.entitlement;

import java.util.UUID;
import java.util.function.Consumer;

public interface PlayerEntitlementService {

    PlayerEntitlementSnapshot snapshot(UUID playerId, long nowMillis);

    void registerListener(Consumer<PlayerEntitlementUpdatedEvent> listener);

    default void invalidate(UUID playerId) {}
}