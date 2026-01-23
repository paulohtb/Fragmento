package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.contentModule.minecraft.FragmentoMinecraftContent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import java.util.concurrent.atomic.AtomicReference;

@Mod(FragmentoMod.MODID)
public final class FragmentoMod {
    public static final String MODID = "fragmento";
    public static final AtomicReference<Object> INTEGRATED_SERVER_INTENTS = new AtomicReference<>();

    public FragmentoMod(IEventBus modBus) {
        FragmentoMinecraftContent.REGISTRY.register(modBus);
    }
}