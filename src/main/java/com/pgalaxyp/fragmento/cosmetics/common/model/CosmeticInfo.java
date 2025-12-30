package com.pgalaxyp.fragmento.cosmetics.common.model;

import java.util.Objects;

public record CosmeticInfo(
        CosmeticId id,
        CosmeticSlot slot,
        int requiredTier,
        int sort,
        boolean visibleToSelf,
        String modelKey,
        String displayName
) {
    public CosmeticInfo {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(slot, "slot");
        Objects.requireNonNull(modelKey, "modelKey");
        Objects.requireNonNull(displayName, "displayName");
        if (requiredTier < 0) {
            throw new IllegalArgumentException("requiredTier");
        }
    }
}