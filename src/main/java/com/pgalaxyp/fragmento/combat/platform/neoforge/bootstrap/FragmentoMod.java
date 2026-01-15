package com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap;


import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.items.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.net.wire.*;

@Mod(FragmentoMod.MOD_ID)
public final class FragmentoMod {

    public static final String MOD_ID = "fragmento";

    public FragmentoMod(IEventBus modBus) {
        ModItemsRegistry.REGISTRY.register(modBus);
        modBus.addListener(NFWire::register);
        modBus.addListener(ModItemsRegistry::addToCreativeTabs);
    }
}