package com.pgalaxyp.fragmento.cosmetics.common.entitlement;

import java.util.UUID;

public record PlayerEntitlementUpdatedEvent(
        UUID playerId,
        int level,
        long version
) {}