package com.pgalaxyp.fragmento.rpg.content;

import com.pgalaxyp.fragmento.rpg.content.catalyst.CatalystRegistration;
import com.pgalaxyp.fragmento.rpg.content.catalyst.ItemCatalystRegistry;
import com.pgalaxyp.fragmento.rpg.content.entity.RpgEntityRegistry;
import net.neoforged.bus.api.IEventBus;

public final class RpgContent {

    public static void register(IEventBus modBus) {
        RpgEntityRegistry.register(modBus);
        ItemCatalystRegistry.register(modBus);
        CatalystRegistration.registerAll();
    }

    private RpgContent() {}
}