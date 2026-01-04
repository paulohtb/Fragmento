package com.pgalaxyp.fragmento.combat.content;

import com.pgalaxyp.fragmento.combat.content.catalyst.FragmentoCatalystDefinitions;
import com.pgalaxyp.fragmento.combat.content.catalyst.FragmentoCatalysts;
import com.pgalaxyp.fragmento.combat.content.entity.FragmentoEntities;
import net.neoforged.bus.api.IEventBus;

public final class FragmentoContent {

    public static void register(IEventBus modBus) {
        FragmentoEntities.register(modBus);
        FragmentoCatalysts.register(modBus);

        FragmentoCatalystDefinitions.registerAll();
    }

    private FragmentoContent() {
    }
}