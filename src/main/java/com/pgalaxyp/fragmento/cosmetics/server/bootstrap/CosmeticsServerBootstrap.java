package com.pgalaxyp.fragmento.cosmetics.server.bootstrap;

import com.pgalaxyp.fragmento.cosmetics.common.definitions.builtin.BuiltinCosmetics;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.CosmeticEntitlementService;
import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticRegistryImpl;
import com.pgalaxyp.fragmento.cosmetics.common.validation.CosmeticValidator;
import com.pgalaxyp.fragmento.cosmetics.server.network.TrackingCosmeticSyncPublisher;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticServiceImpl;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticServices;
import com.pgalaxyp.fragmento.tiers.server.entitlement.TierCosmeticEntitlementService;
import com.pgalaxyp.fragmento.tiers.server.service.TierServices;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@EventBusSubscriber(modid = "fragmento")
public final class CosmeticsServerBootstrap {

    private static volatile boolean bound;

    private CosmeticsServerBootstrap() {}

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        if (bound) {
            return;
        }
        bound = true;

        CosmeticRegistryImpl registry = new CosmeticRegistryImpl();
        registry.setSnapshot(BuiltinCosmetics.snapshot());

        CosmeticEntitlementService entitlements =
                new TierCosmeticEntitlementService(TierServices.service());

        CosmeticValidator validator =
                new CosmeticValidator(registry, entitlements);

        CosmeticServiceImpl service =
                new CosmeticServiceImpl(
                        validator,
                        new TrackingCosmeticSyncPublisher(),
                        registry,
                        entitlements
                );

        TierServices.service().registerListener(service);
        CosmeticServices.bind(service);
    }
}
