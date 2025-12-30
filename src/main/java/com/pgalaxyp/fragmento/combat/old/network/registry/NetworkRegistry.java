package com.pgalaxyp.fragmento.combat.old.network.registry;

import com.pgalaxyp.fragmento.combat.old.client.visual.MinorWindVortexVisualPacketHandler;
import com.pgalaxyp.fragmento.combat.old.network.c2s.SkillC2SPacketHandlers;
import com.pgalaxyp.fragmento.combat.old.network.c2s.SkillCancelPacket;
import com.pgalaxyp.fragmento.combat.old.network.c2s.SkillIntentPacket;
import com.pgalaxyp.fragmento.combat.old.network.s2c.MinorWindVortexVisualPacket;
import com.pgalaxyp.fragmento.combat.old.network.s2c.SkillStateSnapshotPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.MOD)
public final class NetworkRegistry {

    private NetworkRegistry() {}

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                SkillIntentPacket.TYPE,
                SkillIntentPacket.STREAM_CODEC,
                SkillC2SPacketHandlers::handleIntent
        );

        registrar.playToServer(
                SkillCancelPacket.TYPE,
                SkillCancelPacket.STREAM_CODEC,
                SkillC2SPacketHandlers::handleCancel
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