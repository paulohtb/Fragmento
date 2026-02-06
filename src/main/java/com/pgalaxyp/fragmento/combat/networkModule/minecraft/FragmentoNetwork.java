package com.pgalaxyp.fragmento.combat.networkModule.minecraft;

import com.pgalaxyp.fragmento.combat.platformModule.FragmentoPlatform;
import com.pgalaxyp.fragmento.combat.mod.FragmentoServerRuntimeRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber(modid = FragmentoPlatform.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class FragmentoNetwork {
    @SubscribeEvent public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        registrar.playToServer(PrimaryActionPayload.TYPE, PrimaryActionPayload.STREAM_CODEC, FragmentoNetwork::handlePrimaryAction);
    }

    private static void handlePrimaryAction(PrimaryActionPayload payload, IPayloadContext context) {
        var p = context.player();
        if (!(p instanceof ServerPlayer sp)) return;
        FragmentoServerRuntimeRegistry.onPrimaryAction(sp);
    }

    private FragmentoNetwork() {}
}