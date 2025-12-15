package com.pgalaxyp.fragmento.content.bard.client;

import com.pgalaxyp.fragmento.content.bard.client.renderer.FluteSpiritRenderer;
import com.pgalaxyp.fragmento.content.bard.client.renderer.VortexRenderer;
import com.pgalaxyp.fragmento.content.bard.registry.FluteSkillEntityRegistry;
import com.pgalaxyp.fragmento.content.bard.registry.VortexHelperRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class SpiritClientSetup {

    private SpiritClientSetup() {
    }

    public static void init(IEventBus modBus) {
        modBus.addListener(SpiritClientSetup::registerRenderers);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                FluteSkillEntityRegistry.FLUTE_SPIRIT.get(),
                FluteSpiritRenderer::new
        );

        event.registerEntityRenderer(
                VortexHelperRegistry.WIND_VORTEX.get(),
                VortexRenderer::new
        );

        event.registerEntityRenderer(
                VortexHelperRegistry.MEDIUM_WIND_VORTEX.get(),
                VortexRenderer::new
        );
    }
}
