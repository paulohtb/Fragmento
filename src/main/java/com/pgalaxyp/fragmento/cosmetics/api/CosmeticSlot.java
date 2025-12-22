package com.pgalaxyp.fragmento.cosmetics.api;

import net.minecraft.resources.ResourceLocation;
import java.util.Locale;
import java.util.Objects;

public enum CosmeticSlot {

    HEAD(
            ResourceLocation.fromNamespaceAndPath("fragmento", "head"),
            600,
            CosmeticTransform.IDENTITY
    ),
    BODY(
            ResourceLocation.fromNamespaceAndPath("fragmento", "body"),
            500,
            CosmeticTransform.IDENTITY
    ),
    ARMS(
            ResourceLocation.fromNamespaceAndPath("fragmento", "arms"),
            400,
            CosmeticTransform.IDENTITY
    ),
    LEGS(
            ResourceLocation.fromNamespaceAndPath("fragmento", "legs"),
            300,
            CosmeticTransform.IDENTITY
    ),
    FEET(
            ResourceLocation.fromNamespaceAndPath("fragmento", "feet"),
            200,
            CosmeticTransform.IDENTITY
    ),
    CAPE(
            ResourceLocation.fromNamespaceAndPath("fragmento", "cape"),
            100,
            new CosmeticTransform(
                    0.0F,
                    0.0F,
                    0.125F,
                    180.0F,
                    0.0F,
                    0.0F,
                    1.0F
            )
    );

    private final ResourceLocation id;
    private final int priority;
    private final CosmeticTransform defaultTransform;

    CosmeticSlot(ResourceLocation id, int priority, CosmeticTransform defaultTransform) {
        this.id = Objects.requireNonNull(id, "id");
        this.priority = priority;
        this.defaultTransform = defaultTransform == null ? CosmeticTransform.IDENTITY : defaultTransform;
    }

    public ResourceLocation id() {
        return id;
    }

    public int priority() {
        return priority;
    }

    public CosmeticTransform defaultTransform() {
        return defaultTransform;
    }

    public boolean canOverride(CosmeticSlot other) {
        if (other == null) return true;
        return this.priority >= other.priority;
    }

    public static CosmeticSlot byId(ResourceLocation id) {
        if (id == null) return null;
        for (CosmeticSlot slot : values()) {
            if (slot.id.equals(id)) return slot;
        }
        return null;
    }

    public static CosmeticSlot byName(String name) {
        if (name == null) return null;
        String n = name.trim().toUpperCase(Locale.ROOT);
        for (CosmeticSlot slot : values()) {
            if (slot.name().equals(n)) return slot;
        }
        return null;
    }
}