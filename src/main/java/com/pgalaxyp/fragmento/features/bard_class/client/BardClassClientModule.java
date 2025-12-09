package com.pgalaxyp.fragmento.features.bard_class.client;

import com.pgalaxyp.fragmento.features.bard_class.client.registry.SpiritClientSetup;
import net.neoforged.bus.api.IEventBus;

public final class BardClassClientModule {

    private BardClassClientModule() {}

    public static void init(IEventBus modBus) {
        SpiritClientSetup.registerRenderers(modBus);
    }
}
