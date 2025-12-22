package com.pgalaxyp.fragmento.cosmetics.api;

import com.mojang.logging.LogUtils;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public final class CosmeticTypeId {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final ResourceLocation id;

    public CosmeticTypeId(ResourceLocation id) {
        this.id = Objects.requireNonNull(id, "id");
        LOGGER.debug("CosmeticTypeId criado, {}", this.id);
    }

    public static CosmeticTypeId of(ResourceLocation id) {
        return new CosmeticTypeId(id);
    }

    public static CosmeticTypeId parse(String value) {
        Objects.requireNonNull(value, "value");
        ResourceLocation rl = ResourceLocation.tryParse(value);
        if (rl == null) {
            LOGGER.warn("CosmeticTypeId invalido, {}", value);
            throw new IllegalArgumentException("CosmeticTypeId invalido");
        }
        CosmeticTypeId out = new CosmeticTypeId(rl);
        LOGGER.debug("CosmeticTypeId parse, {}", out.id);
        return out;
    }

    public ResourceLocation value() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CosmeticTypeId other)) return false;
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