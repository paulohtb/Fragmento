package com.pgalaxyp.fragmento.features.bard_class.registry;

import com.pgalaxyp.fragmento.features.bard_class.registry.entity.VortexHelperRegistry;
import com.pgalaxyp.fragmento.features.bard_class.registry.entity.FluteItemRegistry;
import com.pgalaxyp.fragmento.features.bard_class.registry.entity.FluteSpiritRegistry;
import net.neoforged.bus.api.IEventBus;

public final class BardClassRegistries {

    private BardClassRegistries() {}

    public static void registerCommon(IEventBus modBus) {
        FluteSpiritRegistry.ENTITIES.register(modBus);
        VortexHelperRegistry.ENTITIES.register(modBus);
        FluteItemRegistry.ITEMS.register(modBus);
    }
}
