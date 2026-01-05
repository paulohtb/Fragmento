package com.pgalaxyp.fragmento.rpg.content;

import com.pgalaxyp.fragmento.rpg.content.catalyst.ItemCatalystRegistry;
import com.pgalaxyp.fragmento.rpg.content.entity.RpgEntityRegistry;
import com.pgalaxyp.fragmento.rpg.content.profile.RpgProfiles;
import net.neoforged.bus.api.IEventBus;

public final class RpgContent {

    public static void register(IEventBus modBus) {
        RpgEntityRegistry.register(modBus);
        ItemCatalystRegistry.register(modBus);
        RpgProfiles.registerAll();
    }

    private RpgContent() {}
}