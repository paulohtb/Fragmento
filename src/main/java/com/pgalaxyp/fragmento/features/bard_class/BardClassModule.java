package com.pgalaxyp.fragmento.features.bard_class;

import com.pgalaxyp.fragmento.features.bard_class.registry.BardClassRegistries;
import net.neoforged.bus.api.IEventBus;

public final class BardClassModule {

    private BardClassModule() {}

    public static void init(IEventBus modBus) {
        BardClassRegistries.registerCommon(modBus);
    }
}
