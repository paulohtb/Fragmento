package com.pgalaxyp.fragmento.tiers.network;

import com.pgalaxyp.fragmento.tiers.server.service.NoopTierService;
import com.pgalaxyp.fragmento.tiers.server.service.TierService;
import com.pgalaxyp.fragmento.tiers.server.service.TierServiceImpl;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

import java.time.Duration;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.GAME)
public final class TierNetworkGameBus {

    private static final String BASE_URL =
            "https://us-central1-fragmento-d4d79.cloudfunctions.net";

    private TierNetworkGameBus() {}

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        TierService service;

        if (!BASE_URL.isEmpty()) {
            TierApiClient api =
                    new TierApiClient(
                            BASE_URL,
                            "",
                            Duration.ofSeconds(5)
                    );
            service = new TierServiceImpl(api);
        } else {
            service = new NoopTierService();
        }

        TierNetwork.bindService(service);
    }
}