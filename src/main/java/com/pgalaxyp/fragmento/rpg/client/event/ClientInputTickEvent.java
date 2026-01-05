package com.pgalaxyp.fragmento.rpg.client.event;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import static com.pgalaxyp.fragmento.bootstrap.FragmentoMod.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public final class ClientInputTickEvent {

    private static ClientInputController controller;

    public static void bind(ClientInputController inputController) {
        controller = inputController;
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (controller == null) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.options == null) return;

        controller.clientTick();

        while (mc.options.keyAttack.consumeClick()) {
            controller.onAttackClick();
        }

        while (mc.options.keyUse.consumeClick()) {
            controller.onUseItemClick();
        }
    }

    private ClientInputTickEvent() {}
}