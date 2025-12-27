package com.pgalaxyp.fragmento.cosmetics.api;

import com.pgalaxyp.fragmento.tiers.api.TierLevel;
import java.util.Objects;

public record CosmeticDefinition(
        CosmeticId id,
        CosmeticTypeId type,
        CosmeticSlot slot,
        TierLevel requiredTier,
        int priority,
        boolean visibleToSelf
) {

    public CosmeticDefinition {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(slot, "slot");
        Objects.requireNonNull(requiredTier, "requiredTier");
    }
}