package com.pgalaxyp.fragmento.cosmetics.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.MOD)
public final class CosmeticNetwork {

    private static volatile com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticService SERVICE;
    private static volatile CosmeticSyncPublisher PUBLISHER;

    private CosmeticNetwork() {
    }

    public static void bindService(com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticService service) {
        bindService(service, null);
    }

    public static void bindService(com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticService service, CosmeticSyncPublisher publisher) {
        SERVICE = service;
        if (publisher != null) {
            PUBLISHER = publisher;
        }
    }

    public static CosmeticSyncPublisher publisher() {
        CosmeticSyncPublisher p = PUBLISHER;
        return p == null ? new DefaultCosmeticSyncPublisher() : p;
    }

    static com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticService service() {
        return SERVICE;
    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                CosmeticSyncPacket.TYPE,
                CosmeticSyncPacket.STREAM_CODEC,
                CosmeticSyncClientHandler::handle
        );

        registrar.playToServer(
                CosmeticSyncRequestPacket.TYPE,
                CosmeticSyncRequestPacket.STREAM_CODEC,
                CosmeticSyncServerHandler::handle
        );

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