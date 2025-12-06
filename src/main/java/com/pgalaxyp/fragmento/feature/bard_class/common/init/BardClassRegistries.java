package com.pgalaxyp.fragmento.feature.bard_class.common.init;

import net.neoforged.bus.api.IEventBus;

public final class BardClassRegistries {

    private BardClassRegistries() {
    }

    public static void registerCommon(IEventBus modBus) {
        FluteSpiritRegistry.ENTITIES.register(modBus);
        FluteVortexRegistry.ENTITIES.register(modBus);
        FluteItemRegistry.ITEMS.register(modBus);
    }
}
