package com.pgalaxyp.fragmento.feature.bard_class.client;

import com.pgalaxyp.fragmento.feature.bard_class.client.render.BardSpiritClientSetup;
import com.pgalaxyp.fragmento.feature.bard_class.client.render.spirit.FluteSpiritRenderer;
import net.neoforged.bus.api.IEventBus;

public final class BardClientModule {

    private BardClientModule() {
    }

    public static void init(IEventBus modBus) {
        FluteSpiritRenderer.register();
        BardSpiritClientSetup.registerRenderers(modBus);
    }
}
