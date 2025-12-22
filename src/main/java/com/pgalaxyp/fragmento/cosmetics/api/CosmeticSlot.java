package com.pgalaxyp.fragmento.cosmetics.api;

import com.mojang.logging.LogUtils;
import java.util.Locale;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public enum CosmeticSlot {

    HEAD(ResourceLocation.fromNamespaceAndPath("fragmento", "head"), 600),
    BODY(ResourceLocation.fromNamespaceAndPath("fragmento", "body"), 500),
    ARMS(ResourceLocation.fromNamespaceAndPath("fragmento", "arms"), 400),
    LEGS(ResourceLocation.fromNamespaceAndPath("fragmento", "legs"), 300),
    FEET(ResourceLocation.fromNamespaceAndPath("fragmento", "feet"), 200),
    CAPE(ResourceLocation.fromNamespaceAndPath("fragmento", "cape"), 100);

    private static final Logger LOGGER = LogUtils.getLogger();

    private final ResourceLocation id;
    private final int priority;

    CosmeticSlot(ResourceLocation id, int priority) {
        this.id = Objects.requireNonNull(id, "id");
        this.priority = priority;
    }

    public ResourceLocation id() {
        return id;
    }

    public int priority() {
        return priority;
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
        LOGGER.debug("CosmeticSlot not found, {}", name);
        return null;
    }
}