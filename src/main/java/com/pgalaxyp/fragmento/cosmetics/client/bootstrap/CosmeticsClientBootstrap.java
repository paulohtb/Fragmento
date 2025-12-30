package com.pgalaxyp.fragmento.cosmetics.client.bootstrap;

import com.pgalaxyp.fragmento.common.progression.PlayerProgressionView;
import com.pgalaxyp.fragmento.cosmetics.client.entitlement.ProgressionBasedCosmeticEntitlementClientView;
import com.pgalaxyp.fragmento.cosmetics.client.render.model.CosmeticsModelsBootstrap;
import com.pgalaxyp.fragmento.cosmetics.client.state.CosmeticsClientEntitlements;
import com.pgalaxyp.fragmento.cosmetics.client.state.CosmeticsClientState;
import com.pgalaxyp.fragmento.common.progression.tiers.TierClientProgressionView;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = "fragmento", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class CosmeticsClientBootstrap {

    private CosmeticsClientBootstrap() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        CosmeticsClientState.clearAll();

        PlayerProgressionView progression = new TierClientProgressionView();
        CosmeticsClientEntitlements.set(
                new ProgressionBasedCosmeticEntitlementClientView(progression)
        );

        CosmeticsModelsBootstrap.bootstrap();
    }
}