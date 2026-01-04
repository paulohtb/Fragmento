package com.pgalaxyp.fragmento.rpg.client.event;

import com.pgalaxyp.fragmento.rpg.content.entity.RpgEntityRegistry;
import com.pgalaxyp.fragmento.rpg.client.render.CutEffectRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import static com.pgalaxyp.fragmento.bootstrap.FragmentoMod.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ClientRenderEvents {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(RpgEntityRegistry.CUT.get(), CutEffectRenderer::new);
    }

    private ClientRenderEvents() {}
}