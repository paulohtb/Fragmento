package com.pgalaxyp.fragmento.cosmetics.client.bootstrap;

import com.pgalaxyp.fragmento.cosmetics.client.entitlement.TierBasedCosmeticEntitlementClientView;
import com.pgalaxyp.fragmento.cosmetics.common.definitions.builtin.BuiltinCosmetics;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.CosmeticEntitlementClientViews;
import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticRegistryImpl;
import com.pgalaxyp.fragmento.cosmetics.client.state.CosmeticsClientRegistries;
import com.pgalaxyp.fragmento.cosmetics.client.render.model.CosmeticsModelsBootstrap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class CosmeticsClientBootstrap {

    private CosmeticsClientBootstrap() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        CosmeticRegistryImpl reg = new CosmeticRegistryImpl();
        reg.setSnapshot(BuiltinCosmetics.snapshot());
        CosmeticsClientRegistries.setRegistry(reg);

        CosmeticEntitlementClientViews.set(new TierBasedCosmeticEntitlementClientView());

        CosmeticsModelsBootstrap.bootstrap();
    }
}