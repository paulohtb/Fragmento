package com.pgalaxyp.fragmento.cosmetic.client.network;

import com.pgalaxyp.fragmento.cosmetic.common.network.CosmeticDeltaSyncPacket;
import com.pgalaxyp.fragmento.cosmetic.common.network.CosmeticListSyncPacket;
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
        PayloadRegistrar registrar = event.registrar("2");

        registrar.playToClient(
                CosmeticListSyncPacket.TYPE,
                CosmeticListSyncPacket.STREAM_CODEC,
                CosmeticSyncClientHandler::handleList
        );

        registrar.playToClient(
                CosmeticDeltaSyncPacket.TYPE,
                CosmeticDeltaSyncPacket.STREAM_CODEC,
                CosmeticSyncClientHandler::handleDelta
        );
    }
}