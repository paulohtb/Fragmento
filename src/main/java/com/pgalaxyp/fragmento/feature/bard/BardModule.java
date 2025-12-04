package com.pgalaxyp.fragmento.feature.bard;

import com.pgalaxyp.fragmento.feature.bard.common.init.BardFeatureInit;
import net.neoforged.bus.api.IEventBus;

public final class BardModule {

    private BardModule() {
    }

    public static void init(IEventBus modBus) {
        BardFeatureInit.registerCommon(modBus);
    }
}
