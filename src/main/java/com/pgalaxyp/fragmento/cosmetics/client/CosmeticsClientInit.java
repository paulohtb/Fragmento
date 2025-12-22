package com.pgalaxyp.fragmento.cosmetics.client;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys;
import com.pgalaxyp.fragmento.cosmetics.network.CosmeticsNetwork;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;

@EventBusSubscriber(modid = CosmeticsKeys.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class CosmeticsClientInit {

    private static final Logger LOGGER = LogUtils.getLogger();

    private CosmeticsClientInit() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        CosmeticsNetwork.setClientHandlers(new CosmeticsClientHandlersImpl());
    }
}