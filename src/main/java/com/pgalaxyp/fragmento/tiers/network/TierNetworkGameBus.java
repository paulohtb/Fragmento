package com.pgalaxyp.fragmento.tiers.network;

import com.pgalaxyp.fragmento.tiers.server.service.NoopTierService;
import com.pgalaxyp.fragmento.tiers.server.service.TierService;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.GAME)
public final class TierNetworkGameBus {

    private TierNetworkGameBus() {
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        if (TierNetwork.service() == null) {
            TierNetwork.bindService(new NoopTierService());
        }
    }
}