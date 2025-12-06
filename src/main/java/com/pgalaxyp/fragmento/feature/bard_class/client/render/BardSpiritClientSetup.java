package com.pgalaxyp.fragmento.feature.bard_class.client.render;

import com.pgalaxyp.fragmento.feature.bard_class.client.render.spirit.FluteSpiritRenderer;
import com.pgalaxyp.fragmento.feature.bard_class.client.render.spirit.FluteVortexRenderer;
import com.pgalaxyp.fragmento.feature.bard_class.common.init.FluteSpiritRegistry;
import com.pgalaxyp.fragmento.feature.bard_class.common.init.FluteVortexRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class BardSpiritClientSetup {

    private BardSpiritClientSetup() {}

    public static void registerRenderers(IEventBus modBus) {
        modBus.addListener(BardSpiritClientSetup::onRegister);
    }

    private static void onRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(FluteSpiritRegistry.FLUTE_SPIRIT.get(), FluteSpiritRenderer::new);
        event.registerEntityRenderer(FluteVortexRegistry.FLUTE_VORTEX.get(), FluteVortexRenderer::new);
    }
}
