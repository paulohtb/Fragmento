package com.pgalaxyp.fragmento.bootstrap;

import com.pgalaxyp.fragmento.rpg.content.RpgContent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(FragmentoMod.MODID)
public final class FragmentoMod {

    public static final String MODID = "fragmento";

    public FragmentoMod(IEventBus modBus) {
        RpgContent.register(modBus);
        RpgNetwork.register(modBus);
        RpgBootstrap.init();
    }
}