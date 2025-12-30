package com.pgalaxyp.fragmento.combat.old.system.core;

import com.pgalaxyp.fragmento.combat.old.content.bard.module.BardModule;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod("fragmento")
public final class FragmentoMod {

    public FragmentoMod(IEventBus modBus) {
        BardModule.init(modBus);
    }
}
