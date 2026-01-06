package com.pgalaxyp.fragmento.rpg.adapter.minecraft.input;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import com.pgalaxyp.fragmento.rpg.gameplay.input.InputAction;
import com.pgalaxyp.fragmento.rpg.network.payload.c2s.InputIntentPayload;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class ClientInputBridge {

    private ClientInputBridge() {}

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        var mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (mc.options.keyAttack.consumeClick()) {
            PacketDistributor.sendToServer(new InputIntentPayload(InputAction.ATTACK_PRIMARY.name()));
        }
    }
}