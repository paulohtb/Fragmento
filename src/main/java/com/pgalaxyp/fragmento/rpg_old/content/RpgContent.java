package com.pgalaxyp.fragmento.rpg_old.content;

import com.pgalaxyp.fragmento.rpg_old.content.catalyst.ItemCatalystRegistry;
import com.pgalaxyp.fragmento.rpg_old.content.entity.RpgEntityRegistry;
import com.pgalaxyp.fragmento.rpg_old.content.profile.RpgProfiles;
import net.neoforged.bus.api.IEventBus;

public final class RpgContent {

    public static void register(IEventBus modBus) {
        RpgEntityRegistry.register(modBus);
        ItemCatalystRegistry.register(modBus);
        RpgProfiles.registerAll();
    }

    private RpgContent() {}
}