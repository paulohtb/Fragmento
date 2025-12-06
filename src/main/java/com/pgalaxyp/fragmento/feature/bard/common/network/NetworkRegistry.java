package com.pgalaxyp.fragmento.feature.bard.common.network;

import com.pgalaxyp.fragmento.feature.bard.common.network.packet.BasicAbilityPacket;
import com.pgalaxyp.fragmento.feature.bard.common.network.packet.ChargedAbilityPacket;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.MOD)
public final class NetworkRegistry {

    private NetworkRegistry() {
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", path);
    }

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                BasicAbilityPacket.TYPE,
                BasicAbilityPacket.STREAM_CODEC,
                BasicAbilityPacket::handle
        );

        registrar.playToServer(
                ChargedAbilityPacket.TYPE,
                ChargedAbilityPacket.STREAM_CODEC,
                ChargedAbilityPacket::handle
        );
    }
}
