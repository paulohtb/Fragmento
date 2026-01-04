package com.pgalaxyp.fragmento.combat.client;

import com.pgalaxyp.fragmento.combat.client.render.CutRenderer;
import com.pgalaxyp.fragmento.combat.content.entity.FragmentoEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import static com.pgalaxyp.fragmento.bootstrap.FragmentoMod.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class FragmentoClientRenderModEvents {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(FragmentoEntities.CUT.get(), CutRenderer::new);
    }

    private FragmentoClientRenderModEvents() {
    }
}