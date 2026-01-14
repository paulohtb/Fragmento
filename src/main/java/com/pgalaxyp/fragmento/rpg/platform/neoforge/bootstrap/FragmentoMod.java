package com.pgalaxyp.fragmento.rpg.platform.neoforge.bootstrap;

import com.pgalaxyp.fragmento.rpg.platform.neoforge.items.FragmentoItems;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.net.wire.NeoForgeNetWire;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(FragmentoMod.MOD_ID)
public final class FragmentoMod {

    public static final String MOD_ID = "fragmento";

    public FragmentoMod(IEventBus modBus) {
        FragmentoItems.REGISTRY.register(modBus);
        modBus.addListener(NeoForgeNetWire::register);
        modBus.addListener(FragmentoItems::addToCreativeTabs);
    }
}