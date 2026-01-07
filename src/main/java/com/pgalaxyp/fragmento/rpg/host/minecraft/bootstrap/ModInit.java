package com.pgalaxyp.fragmento.rpg.host.minecraft.bootstrap;

import com.pgalaxyp.fragmento.rpg.host.minecraft.events.VanillaEventBlocker;
import net.neoforged.bus.api.IEventBus;

public final class ModInit {

    public static void init(IEventBus bus) {
        bus.register(new VanillaEventBlocker());
    }
}