package com.pgalaxyp.fragmento.tiers.server.bootstrap;

import com.pgalaxyp.fragmento.cosmetics.server.bootstrap.CosmeticsServerWiring;
import com.pgalaxyp.fragmento.tiers.server.entitlement.TierEntitlementServiceAdapter;
import com.pgalaxyp.fragmento.tiers.server.http.TierApiClient;
import com.pgalaxyp.fragmento.tiers.server.service.TierServiceImpl;
import com.pgalaxyp.fragmento.tiers.server.service.TierServices;
import com.pgalaxyp.fragmento.tiers.server.sync.TierServerSync;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@EventBusSubscriber(modid = "fragmento")
public final class TiersServerBootstrap {

    private static volatile boolean bound;

    private TiersServerBootstrap() {}

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerStarted(ServerStartedEvent event) {
        if (bound) {
            return;
        }
        bound = true;

        TierApiClient api = new TierApiClient();
        TierServiceImpl service = new TierServiceImpl(api);

        TierServices.bind(service);
        TierServerSync.bindListenerOnce(service);

        CosmeticsServerWiring.bindEntitlements(
                new TierEntitlementServiceAdapter(service)
        );
    }
}