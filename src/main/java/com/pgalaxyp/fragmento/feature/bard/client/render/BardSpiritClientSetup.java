package com.pgalaxyp.fragmento.feature.bard.client.render;

import com.pgalaxyp.fragmento.feature.bard.client.render.spirit.FluteSpiritRenderer;
import com.pgalaxyp.fragmento.feature.bard.common.init.FluteSpiritTypeRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class BardSpiritClientSetup {

    private BardSpiritClientSetup() {
    }

    public static void registerRenderers(IEventBus modBus) {
        modBus.addListener(BardSpiritClientSetup::registerRenderer);
    }

    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                FluteSpiritTypeRegistry.FLUTE_SPIRIT.get(),
                FluteSpiritRenderer::new
        );
    }
}
