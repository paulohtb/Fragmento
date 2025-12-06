package com.pgalaxyp.fragmento.feature.bard.common.init;

import net.neoforged.bus.api.IEventBus;

public final class BardFeatureInit {

    private BardFeatureInit() {
    }

    public static void registerCommon(IEventBus modBus) {
        FluteSpiritRegistry.ENTITIES.register(modBus);
        FluteItemRegistry.ITEMS.register(modBus);
    }
}