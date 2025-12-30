package com.pgalaxyp.fragmento.cosmetic.client.bootstrap;

import com.pgalaxyp.fragmento.cosmetic.client.render.model.CosmeticsModelsBootstrap;
import com.pgalaxyp.fragmento.cosmetic.client.state.ClientCosmetics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = "fragmento", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class CosmeticsClientBootstrap {

    private CosmeticsClientBootstrap() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ClientCosmetics.clearAll();
        CosmeticsModelsBootstrap.bootstrap();
    }
}