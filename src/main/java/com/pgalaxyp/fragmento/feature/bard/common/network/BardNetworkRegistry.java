package com.pgalaxyp.fragmento.feature.bard.common.network;

import com.pgalaxyp.fragmento.feature.bard.common.network.packet.BardBasicAbilityPacket;
import com.pgalaxyp.fragmento.feature.bard.common.network.packet.BardChargedAbilityPacket;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.MOD)
public final class BardNetworkRegistry {

    private BardNetworkRegistry() {
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath("fragmento", path);
    }

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                BardBasicAbilityPacket.TYPE,
                BardBasicAbilityPacket.STREAM_CODEC,
                BardBasicAbilityPacket::handle
        );

        registrar.playToServer(
                BardChargedAbilityPacket.TYPE,
                BardChargedAbilityPacket.STREAM_CODEC,
                BardChargedAbilityPacket::handle
        );
    }
}
