package com.pgalaxyp.fragmento.cosmetics.server.bootstrap;

import com.pgalaxyp.fragmento.common.progression.PlayerProgressionView;
import com.pgalaxyp.fragmento.cosmetics.common.definitions.builtin.BuiltinCosmetics;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.ProgressionBasedCosmeticEntitlementCore;
import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticRegistryImpl;
import com.pgalaxyp.fragmento.cosmetics.common.validation.CosmeticValidator;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticServiceImpl;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticServices;
import com.pgalaxyp.fragmento.cosmetics.server.sync.CosmeticSyncRuntime;
import com.pgalaxyp.fragmento.common.progression.tiers.TierProgressionView;
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

        PlayerProgressionView progression =
                new TierProgressionView(TierServices.service());

        ProgressionBasedCosmeticEntitlementCore entitlements =
                new ProgressionBasedCosmeticEntitlementCore(progression);

        CosmeticValidator validator =
                new CosmeticValidator(registry, entitlements);

        CosmeticServiceImpl service =
                new CosmeticServiceImpl(
                        validator,
                        registry,
                        entitlements
                );

        TierServices.service().registerListener(ev ->
                service.onProgressionChanged(ev.playerId())
        );

        CosmeticServices.bind(service);
        CosmeticSyncRuntime.bind(service);
    }
}