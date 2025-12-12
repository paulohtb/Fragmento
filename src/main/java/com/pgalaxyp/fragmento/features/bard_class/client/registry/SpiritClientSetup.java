package com.pgalaxyp.fragmento.features.bard_class.client.registry;

import com.pgalaxyp.fragmento.features.bard_class.client.render.renderer.FluteSpiritRenderer;
import com.pgalaxyp.fragmento.features.bard_class.client.render.renderer.MediumWindVortexRender;
import com.pgalaxyp.fragmento.features.bard_class.client.render.renderer.MelodyZoneRenderer;
import com.pgalaxyp.fragmento.features.bard_class.client.render.renderer.MinorWindVortexRenderer;
import com.pgalaxyp.fragmento.features.bard_class.registry.entity.VortexHelperRegistry;
import com.pgalaxyp.fragmento.features.bard_class.registry.entity.FluteSpiritRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class SpiritClientSetup {

    private SpiritClientSetup() {
    }

    public static void registerRenderers(IEventBus modBus) {
        modBus.addListener(SpiritClientSetup::onRegister);
    }

    private static void onRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(FluteSpiritRegistry.FLUTE_SPIRIT.get(), FluteSpiritRenderer::new);
        event.registerEntityRenderer(VortexHelperRegistry.WIND_VORTEX.get(), MinorWindVortexRenderer::new);
        event.registerEntityRenderer(VortexHelperRegistry.MELODY_ZONE.get(), MelodyZoneRenderer::new);
        event.registerEntityRenderer(VortexHelperRegistry.MEDIUM_WIND_VORTEX.get(), MediumWindVortexRender::new);
    }
}
