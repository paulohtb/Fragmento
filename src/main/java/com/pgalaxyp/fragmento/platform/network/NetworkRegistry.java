package com.pgalaxyp.fragmento.platform.network;

import com.pgalaxyp.fragmento.platform.network.packet.SkillPacket;
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
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                SkillPacket.TYPE,
                SkillPacket.STREAM_CODEC,
                SkillPacket::handle
        );
    }
}
