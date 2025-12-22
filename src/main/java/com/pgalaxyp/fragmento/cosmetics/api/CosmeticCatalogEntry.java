package com.pgalaxyp.fragmento.cosmetics.api;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;

public record CosmeticCatalogEntry(ResourceLocation id, ResourceLocation type, int slotOrdinal, int requiredTierLevel, int priority, boolean visibleToSelf) {

    public CosmeticCatalogEntry {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(type, "type");
    }

    public CosmeticId cosmeticId() {
        return CosmeticId.of(id);
    }

    public CosmeticTypeId cosmeticTypeId() {
        return CosmeticTypeId.of(type);
    }

    public CosmeticSlot slot() {
        CosmeticSlot[] values = CosmeticSlot.values();
        int i = slotOrdinal;
        if (i < 0) return null;
        if (i >= values.length) return null;
        return values[i];
    }

    public CosmeticTier requiredTier() {
        return CosmeticTier.fromLevel(requiredTierLevel);
    }
}