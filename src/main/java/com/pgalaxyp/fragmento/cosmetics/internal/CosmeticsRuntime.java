package com.pgalaxyp.fragmento.cosmetics.internal;

import com.mojang.logging.LogUtils;
import java.util.Objects;
import org.slf4j.Logger;

public final class CosmeticsRuntime {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static volatile CosmeticRegistryImpl REGISTRY;

    private CosmeticsRuntime() {
    }

    public static void setRegistry(CosmeticRegistryImpl registry) {
        REGISTRY = Objects.requireNonNull(registry, "registry");
        LOGGER.info("CosmeticsRuntime setRegistry ok");
    }

    public static CosmeticRegistryImpl registry() {
        CosmeticRegistryImpl r = REGISTRY;
        if (r == null) {
            LOGGER.warn("CosmeticsRuntime registry null");
            throw new IllegalStateException("CosmeticsRuntime registry null");
        }
        return r;
    }
}