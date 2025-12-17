package com.pgalaxyp.fragmento.system.core;

import com.pgalaxyp.fragmento.content.bard.BardModule;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod("fragmento")
public final class FragmentoMod {

    public FragmentoMod(IEventBus modBus) {
        BardModule.init(modBus);
    }
}
