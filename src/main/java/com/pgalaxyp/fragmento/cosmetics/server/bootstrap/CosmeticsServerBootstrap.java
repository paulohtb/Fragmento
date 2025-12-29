package com.pgalaxyp.fragmento.cosmetics.server.bootstrap;

import com.pgalaxyp.fragmento.cosmetics.server.network.TrackingCosmeticSyncPublisher;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticServices;
import com.pgalaxyp.fragmento.cosmetics.common.definitions.builtin.BuiltinCosmetics;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.LevelAccessPolicies;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.PlayerEntitlementService;
import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticRegistryImpl;
import com.pgalaxyp.fragmento.cosmetics.common.validation.CosmeticValidator;
import com.pgalaxyp.fragmento.cosmetics.common.validation.EntitlementServicePlayerLevelResolver;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticServiceImpl;
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

        PlayerEntitlementService entitlements = CosmeticsServerWiring.entitlements();

        EntitlementServicePlayerLevelResolver resolver = new EntitlementServicePlayerLevelResolver(entitlements);
        CosmeticValidator validator = new CosmeticValidator(registry, resolver, LevelAccessPolicies.DEFAULT);

        CosmeticServiceImpl service = new CosmeticServiceImpl(
                registry,
                entitlements,
                validator,
                new TrackingCosmeticSyncPublisher(),
                LevelAccessPolicies.DEFAULT
        );

        CosmeticServices.bind(service);
    }
}