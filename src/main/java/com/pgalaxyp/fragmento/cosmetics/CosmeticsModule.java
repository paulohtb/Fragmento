package com.pgalaxyp.fragmento.cosmetics;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.internal.CosmeticsRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(CosmeticsKeys.MOD_ID)
public final class CosmeticsModule {

    private static final Logger LOGGER = LogUtils.getLogger();

    public CosmeticsModule(IEventBus modBus) {
        CosmeticsRegistries.register(modBus);
        LOGGER.info("CosmeticsModule iniciado");
    }
}