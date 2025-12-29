package com.pgalaxyp.fragmento.cosmetics.client.network;

import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticSyncPacket;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class CosmeticsNetworkClientBootstrap {

    private CosmeticsNetworkClientBootstrap() {}

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                CosmeticSyncPacket.TYPE,
                CosmeticSyncPacket.STREAM_CODEC,
                CosmeticSyncClientHandler::handle
        );
    }
}