package com.pgalaxyp.fragmento.cosmetics.api;

import java.util.Locale;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;

public record CosmeticCatalogEntry(
        ResourceLocation id,
        ResourceLocation type,
        int slotOrdinal,
        int requiredTierLevel,
        int priority,
        boolean visibleToSelf
) {
    public CosmeticCatalogEntry {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(type, "type");
        CosmeticSlot[] values = CosmeticSlot.values();
        if (slotOrdinal < 0 || slotOrdinal >= values.length) {
            throw new IllegalArgumentException("slotOrdinal");
        }
        if (requiredTierLevel < 0) requiredTierLevel = 0;
        if (requiredTierLevel > 3) requiredTierLevel = 3;
    }

    public CosmeticId cosmeticId() {
        return CosmeticId.of(id);
    }

    public CosmeticTypeId cosmeticTypeId() {
        return CosmeticTypeId.of(type);
    }

    public CosmeticSlot slot() {
        return CosmeticSlot.values()[slotOrdinal];
    }

    public CosmeticTier requiredTier() {
        return CosmeticTier.fromLevel(requiredTierLevel);
    }

    public static CosmeticSlot slotFromName(String name) {
        if (name == null) return null;
        String n = name.trim().toUpperCase(Locale.ROOT);
        for (CosmeticSlot slot : CosmeticSlot.values()) {
            if (slot.name().equals(n)) return slot;
        }
        return null;
    }
}