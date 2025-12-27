package com.pgalaxyp.fragmento.cosmetics.server.bootstrap;

import com.pgalaxyp.fragmento.cosmetics.internal.builtin.BuiltinCosmetics;
import com.pgalaxyp.fragmento.cosmetics.internal.registry.CosmeticRegistryImpl;
import com.pgalaxyp.fragmento.cosmetics.network.CosmeticNetwork;
import com.pgalaxyp.fragmento.cosmetics.network.CosmeticSyncPublisher;
import com.pgalaxyp.fragmento.cosmetics.policy.CosmeticAccessPolicy;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticServiceImpl;
import com.pgalaxyp.fragmento.tiers.server.service.NoopTierService;
import com.pgalaxyp.fragmento.cosmetics.server.validation.CosmeticValidator;
import com.pgalaxyp.fragmento.cosmetics.server.validation.PlayerTierResolver;
import com.pgalaxyp.fragmento.cosmetics.server.validation.TierServicePlayerTierResolver;
import com.pgalaxyp.fragmento.tiers.server.service.TierService;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@EventBusSubscriber(modid = "fragmento")
public final class CosmeticsServerBootstrap {

    private static volatile boolean bound;

    private CosmeticsServerBootstrap() {}

    @SubscribeEvent
    public static void onServerStarted(final ServerStartedEvent event) {
        if (bound) return;
        bound = true;

        CosmeticRegistryImpl registry = new CosmeticRegistryImpl();
        registry.setSnapshot(BuiltinCosmetics.snapshot());

        TierService tierService = new NoopTierService();
        PlayerTierResolver resolver = new TierServicePlayerTierResolver(tierService);

        CosmeticValidator validator = new CosmeticValidator(registry, resolver, new CosmeticAccessPolicy());
        CosmeticSyncPublisher publisher = CosmeticNetwork.publisher();

        CosmeticServiceImpl service = new CosmeticServiceImpl(registry, tierService, validator, publisher);
        CosmeticNetwork.bindService(service, publisher);
    }
}