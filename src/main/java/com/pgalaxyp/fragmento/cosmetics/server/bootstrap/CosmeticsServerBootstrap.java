package com.pgalaxyp.fragmento.cosmetics.server.bootstrap;

import com.pgalaxyp.fragmento.bridge.progression.PlayerProgressionView;
import com.pgalaxyp.fragmento.bridge.progression.tiers.TierProgressionView;
import com.pgalaxyp.fragmento.cosmetics.common.model.BuiltinCosmeticCatalog;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticCatalog;
import com.pgalaxyp.fragmento.cosmetics.server.network.CosmeticsNetPublisher;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticServices;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticsServiceImpl;
import com.pgalaxyp.fragmento.tiers.server.service.TierServices;
import com.pgalaxyp.fragmento.tiers.common.service.TierService;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@EventBusSubscriber(modid = "fragmento")
public final class CosmeticsServerBootstrap {

    private static volatile boolean bound;

    private CosmeticsServerBootstrap() {}

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        if (bound) return;
        bound = true;

        TierService tiers = TierServices.service();
        if (tiers == null) return;

        CosmeticCatalog catalog = new BuiltinCosmeticCatalog();
        PlayerProgressionView progression = new TierProgressionView(tiers);

        CosmeticsServiceImpl service = new CosmeticsServiceImpl(
                catalog,
                progression,
                new CosmeticsNetPublisher()
        );

        tiers.registerListener(ev -> {
            if (ev == null) return;
            service.onTierChanged(ev.playerId());
        });

        CosmeticServices.bind(service);
    }
}