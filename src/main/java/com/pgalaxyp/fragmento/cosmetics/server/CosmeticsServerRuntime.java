package com.pgalaxyp.fragmento.cosmetics.server;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.server.tier.PlayerTierService;
import org.slf4j.Logger;

public final class CosmeticsServerRuntime {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static volatile PlayerTierService TIER_SERVICE;
    private static volatile CosmeticServiceImpl COSMETICS_SERVICE;

    private CosmeticsServerRuntime() {
    }

    public static void set(PlayerTierService tierService, CosmeticServiceImpl cosmeticsService) {
        TIER_SERVICE = tierService;
        COSMETICS_SERVICE = cosmeticsService;
        LOGGER.info("CosmeticsServerRuntime set ok");
    }

    public static PlayerTierService tierService() {
        PlayerTierService s = TIER_SERVICE;
        if (s == null) {
            LOGGER.warn("CosmeticsServerRuntime tierService null");
            throw new IllegalStateException("tierService null");
        }
        return s;
    }

    public static CosmeticServiceImpl cosmeticsService() {
        CosmeticServiceImpl s = COSMETICS_SERVICE;
        if (s == null) {
            LOGGER.warn("CosmeticsServerRuntime cosmeticsService null");
            throw new IllegalStateException("cosmeticsService null");
        }
        return s;
    }
}