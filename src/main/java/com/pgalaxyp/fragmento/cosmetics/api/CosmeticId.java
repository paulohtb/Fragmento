package com.pgalaxyp.fragmento.cosmetics.api;

import com.mojang.logging.LogUtils;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public final class CosmeticId {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final ResourceLocation id;

    public CosmeticId(ResourceLocation id) {
        this.id = Objects.requireNonNull(id, "id");
        LOGGER.debug("CosmeticId criado, {}", this.id);
    }

    public static CosmeticId of(ResourceLocation id) {
        return new CosmeticId(id);
    }

    public static CosmeticId parse(String value) {
        Objects.requireNonNull(value, "value");
        ResourceLocation rl = ResourceLocation.tryParse(value);
        if (rl == null) {
            LOGGER.warn("CosmeticId invalido, {}", value);
            throw new IllegalArgumentException("CosmeticId invalido");
        }
        CosmeticId out = new CosmeticId(rl);
        LOGGER.debug("CosmeticId parse, {}", out.id);
        return out;
    }

    public ResourceLocation value() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CosmeticId other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id.toString();
    }
}