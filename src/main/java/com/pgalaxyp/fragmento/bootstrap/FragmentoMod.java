package com.pgalaxyp.fragmento.bootstrap;

import com.pgalaxyp.fragmento.rpg.host.minecraft.ModEntrypoint;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(FragmentoMod.MODID)
public final class FragmentoMod {

    public static final String MODID = "fragmento";

    public FragmentoMod(IEventBus modBus) {
        new ModEntrypoint(modBus);
    }
}