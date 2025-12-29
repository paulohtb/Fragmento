package com.pgalaxyp.fragmento.cosmetics.server.service;

import java.util.Objects;

public final class CosmeticServices {

    private static volatile CosmeticService SERVICE;

    private CosmeticServices() {}

    public static void bind(CosmeticService service) {
        SERVICE = Objects.requireNonNull(service, "service");
    }

    public static CosmeticService service() {
        return SERVICE;
    }
}