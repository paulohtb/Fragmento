package com.pgalaxyp.fragmento.feature.bard.client;

import com.pgalaxyp.fragmento.feature.bard.client.input.feature.bard.KeyMappings;
import com.pgalaxyp.fragmento.feature.bard.client.render.BardSpiritClientSetup;
import net.neoforged.bus.api.IEventBus;

public final class BardClientModule {

    private BardClientModule() {
    }

    public static void init(IEventBus modBus) {
        BardSpiritClientSetup.registerRenderers(modBus);
        modBus.addListener(KeyMappings::register);
    }
}
