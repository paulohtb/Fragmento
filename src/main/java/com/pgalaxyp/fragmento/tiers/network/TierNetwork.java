package com.pgalaxyp.fragmento.tiers.network;

import com.pgalaxyp.fragmento.tiers.server.service.TierService;

public final class TierNetwork {

    private static volatile TierService SERVICE;

    private TierNetwork() {
    }

    public static void bindService(TierService service) {
        SERVICE = service;
    }

    public static TierService service() {
        return SERVICE;
    }
}