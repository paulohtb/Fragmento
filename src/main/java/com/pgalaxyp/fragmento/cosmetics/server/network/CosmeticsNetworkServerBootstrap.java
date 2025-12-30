package com.pgalaxyp.fragmento.cosmetics.server.network;

import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticEquipRequestPacket;
import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticUnequipRequestPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.MOD)
public final class CosmeticsNetworkServerBootstrap {

    private CosmeticsNetworkServerBootstrap() {}

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                CosmeticEquipRequestPacket.TYPE,
                CosmeticEquipRequestPacket.STREAM_CODEC,
                CosmeticEquipServerHandler::handle
        );

        registrar.playToServer(
                CosmeticUnequipRequestPacket.TYPE,
                CosmeticUnequipRequestPacket.STREAM_CODEC,
                CosmeticUnequipServerHandler::handle
        );
    }
}