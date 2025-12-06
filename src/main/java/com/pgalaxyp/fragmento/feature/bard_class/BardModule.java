package com.pgalaxyp.fragmento.feature.bard_class;

import com.pgalaxyp.fragmento.feature.bard_class.common.init.BardClassRegistries;
import net.neoforged.bus.api.IEventBus;

public final class BardModule {

    private BardModule() {
    }

    public static void init(IEventBus modBus) {
        BardClassRegistries.registerCommon(modBus);
    }
}
