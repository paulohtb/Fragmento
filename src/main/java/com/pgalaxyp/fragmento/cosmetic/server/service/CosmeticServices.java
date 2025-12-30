package com.pgalaxyp.fragmento.cosmetic.server.service;

import java.util.Objects;

public final class CosmeticServices {

    private static volatile CosmeticsService SERVICE;

    private CosmeticServices() {}

    public static void bind(CosmeticsService service) {
        SERVICE = Objects.requireNonNull(service, "service");
    }

    public static CosmeticsService service() {
        return SERVICE;
    }
}