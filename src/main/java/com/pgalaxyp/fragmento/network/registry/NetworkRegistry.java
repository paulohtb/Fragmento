package com.pgalaxyp.fragmento.network.registry;

import com.pgalaxyp.fragmento.client.visual.MinorWindVortexVisualPacketHandler;
import com.pgalaxyp.fragmento.network.c2s.SkillCancelPacket;
import com.pgalaxyp.fragmento.network.c2s.SkillIntentPacket;
import com.pgalaxyp.fragmento.network.s2c.MinorWindVortexVisualPacket;
import com.pgalaxyp.fragmento.network.s2c.SkillStateSnapshotPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.MOD)
public final class NetworkRegistry {

    private NetworkRegistry() {
    }

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                SkillIntentPacket.TYPE,
                SkillIntentPacket.STREAM_CODEC,
                SkillIntentPacket::handle
        );

        registrar.playToServer(
                SkillCancelPacket.TYPE,
                SkillCancelPacket.STREAM_CODEC,
                SkillCancelPacket::handle
        );

        registrar.playToClient(
                SkillStateSnapshotPacket.TYPE,
                SkillStateSnapshotPacket.STREAM_CODEC,
                SkillStateSnapshotPacket::handle
        );

        registrar.playToClient(
                MinorWindVortexVisualPacket.TYPE,
                MinorWindVortexVisualPacket.STREAM_CODEC,
                MinorWindVortexVisualPacketHandler::handle
        );
    }
}