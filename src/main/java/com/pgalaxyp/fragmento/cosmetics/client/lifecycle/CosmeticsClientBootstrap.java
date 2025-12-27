package com.pgalaxyp.fragmento.cosmetics.client.lifecycle;

import com.pgalaxyp.fragmento.cosmetics.client.render.CosmeticsModelsBootstrap;
import com.pgalaxyp.fragmento.cosmetics.client.render.CosmeticsClientRegistryAccess;
import com.pgalaxyp.fragmento.cosmetics.internal.builtin.BuiltinCosmetics;
import com.pgalaxyp.fragmento.cosmetics.internal.registry.CosmeticRegistryImpl;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class CosmeticsClientBootstrap {

    private CosmeticsClientBootstrap() {}

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        CosmeticRegistryImpl reg = new CosmeticRegistryImpl();
        reg.setSnapshot(BuiltinCosmetics.snapshot());
        CosmeticsClientRegistryAccess.setRegistry(reg);
        CosmeticsModelsBootstrap.bootstrap();
    }
}