package com.pgalaxyp.fragmento.cosmetics.common.model;

import java.util.Objects;

public record CosmeticDefinition(
        CosmeticId id,
        CosmeticTypeId type,
        CosmeticSlot slot,
        int requiredLevel,
        int priority,
        boolean visibleToSelf
) {

    public CosmeticDefinition {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(slot, "slot");
        if (requiredLevel < 0) {
            throw new IllegalArgumentException("requiredLevel");
        }
    }
}