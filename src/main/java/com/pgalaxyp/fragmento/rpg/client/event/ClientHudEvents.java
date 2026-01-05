package com.pgalaxyp.fragmento.rpg.client.event;

import com.pgalaxyp.fragmento.bootstrap.FragmentoMod;
import com.pgalaxyp.fragmento.rpg.client.ui.InfusedHudOverlay;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(
        modid = FragmentoMod.MODID,
        value = Dist.CLIENT,
        bus = EventBusSubscriber.Bus.GAME
)
public final class ClientHudEvents {

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        InfusedHudOverlay.render(event.getGuiGraphics());
    }

    private ClientHudEvents() {}
}