package com.pgalaxyp.fragmento.cosmetics.server;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTier;
import com.pgalaxyp.fragmento.cosmetics.internal.CosmeticRegistryImpl;
import com.pgalaxyp.fragmento.cosmetics.internal.CosmeticValidator;
import com.pgalaxyp.fragmento.cosmetics.internal.access.CosmeticAccessPolicy;
import com.pgalaxyp.fragmento.cosmetics.internal.access.PlayerTierResolver;
import com.pgalaxyp.fragmento.cosmetics.server.tier.*;

import java.util.Objects;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

public final class CosmeticsServerWiring implements PlayerTierResolver, PlayerTierUpdateListener {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final CosmeticRegistryImpl registry;
    private final CosmeticAccessPolicy accessPolicy;
    private final TierApiConfig tierApiConfig;
    private final TierApiClient tierApiClient;
    private final PlayerTierService tierService;
    private final CosmeticValidator validator;

    private volatile MinecraftServer server;
    private volatile CosmeticServiceImpl cosmeticService;

    public CosmeticsServerWiring(CosmeticRegistryImpl registry) {
        this.registry = Objects.requireNonNull(registry, "registry");
        this.accessPolicy = new CosmeticAccessPolicy();
        this.tierApiConfig = new TierApiConfig();
        this.tierApiClient = new TierApiClient(tierApiConfig);
        this.tierService = new PlayerTierService(tierApiClient, this);
        this.validator = new CosmeticValidator(this.registry, this, this.accessPolicy);
    }

    public void attachToServer(MinecraftServer server) {
        this.server = Objects.requireNonNull(server, "server");
        this.cosmeticService = new CosmeticServiceImpl(server, registry, validator, tierService);
    }

    public CosmeticServiceImpl cosmeticService() {
        CosmeticServiceImpl s = cosmeticService;
        if (s == null) throw new IllegalStateException("cosmeticService null");
        return s;
    }

    public PlayerTierService tierService() {
        return tierService;
    }

    public TierApiConfig tierApiConfig() {
        return tierApiConfig;
    }

    @Override
    public CosmeticTier getTier(UUID playerId) {
        Objects.requireNonNull(playerId, "playerId");
        MinecraftServer srv = server;
        if (srv == null) return CosmeticTier.TIER_0;
        ServerPlayer player = srv.getPlayerList().getPlayer(playerId);
        if (player == null) return CosmeticTier.TIER_0;
        com.pgalaxyp.fragmento.cosmetics.server.tier.PlayerTierAttachment att = PlayerTierStorage.get(player);
        return att != null ? att.tier() : CosmeticTier.TIER_0;
    }

    @Override
    public void onTierApplied(MinecraftServer server, ServerPlayer player, CosmeticTier tier) {
        CosmeticServiceImpl svc = cosmeticService;
        if (svc == null) return;
        svc.onTierUpdated(player, tier);
    }
}