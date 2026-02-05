package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.platformModule.FragmentoPlatform;
import com.pgalaxyp.fragmento.combat.contentModule.minecraft.FragmentoMinecraftContent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(FragmentoPlatform.MODID)
public final class FragmentoMod {
    public FragmentoMod(IEventBus modBus) {
        FragmentoMinecraftContent.REGISTRY.register(modBus);
    }
}