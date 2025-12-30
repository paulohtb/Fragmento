package com.pgalaxyp.fragmento.tier.client.sync;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.MOD)
public final class TierNetworkBootstrap {

    private TierNetworkBootstrap() {}

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                TierLevelSyncPacket.TYPE,
                TierLevelSyncPacket.STREAM_CODEC,
                TierSyncClientHandler::handle
        );
    }
}