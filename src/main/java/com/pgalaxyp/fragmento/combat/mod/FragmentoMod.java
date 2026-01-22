package com.pgalaxyp.fragmento.combat.mod;

import java.util.concurrent.atomic.AtomicReference;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(FragmentoMod.MODID)
public final class FragmentoMod {
    public static final String MODID = "fragmento";

    public static final AtomicReference<Object> INTEGRATED_SERVER_INTENTS = new AtomicReference<>();

    public FragmentoMod(IEventBus modBus) {}
}