package com.pgalaxyp.fragmento.rpg.host.minecraft;

import com.pgalaxyp.fragmento.rpg.network.payload.c2s.InputIntentPayload;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class ClientInputBridge {

    private ClientInputBridge() {}

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        var mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (mc.options.keyAttack.consumeClick()) {
            PacketDistributor.sendToServer(new InputIntentPayload("ATTACK"));
        }
    }
}