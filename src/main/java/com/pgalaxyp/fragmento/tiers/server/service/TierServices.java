package com.pgalaxyp.fragmento.tiers.server.service;

import com.pgalaxyp.fragmento.tiers.common.service.TierService;
import java.util.Objects;

public final class TierServices {

    private static volatile TierService SERVICE;

    private TierServices() {}

    public static void bind(TierService service) {
        SERVICE = Objects.requireNonNull(service);
    }

    public static TierService service() {
        return SERVICE;
    }
}