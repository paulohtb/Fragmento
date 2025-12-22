package com.pgalaxyp.fragmento.cosmetics.server;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.internal.CosmeticRegistryImpl;
import com.pgalaxyp.fragmento.cosmetics.internal.CosmeticsRuntime;
import com.pgalaxyp.fragmento.cosmetics.network.CosmeticsNetwork;
import com.pgalaxyp.fragmento.cosmetics.server.commands.CosmeticsCommands;
import com.pgalaxyp.fragmento.cosmetics.server.tier.TierApiConfig;
import com.pgalaxyp.fragmento.cosmetics.server.tier.TierApiConfigLoader;
import com.pgalaxyp.fragmento.cosmetics.player.PlayerCosmeticsAttachment;
import com.pgalaxyp.fragmento.cosmetics.player.PlayerCosmeticsStorage;
import com.pgalaxyp.fragmento.cosmetics.server.tier.PlayerTierAttachment;
import com.pgalaxyp.fragmento.cosmetics.server.tier.PlayerTierStorage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.slf4j.Logger;

@EventBusSubscriber(modid = com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class CosmeticsServerEvents {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static volatile CosmeticsServerWiring WIRING;

    private CosmeticsServerEvents() {
    }

    @net.neoforged.bus.api.SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        CosmeticRegistryImpl registry = CosmeticsRuntime.registry();
        CosmeticsServerWiring wiring = new CosmeticsServerWiring(registry);
        wiring.attachToServer(server);

        TierApiConfig cfg = wiring.tierApiConfig();
        TierApiConfigLoader.loadInto(cfg);

        CosmeticsServerRuntime.set(wiring.tierService(), wiring.cosmeticService());
        WIRING = wiring;
    }

    @net.neoforged.bus.api.SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CosmeticsCommands.register(event.getDispatcher());
    }

    @net.neoforged.bus.api.SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        try {
            CosmeticServiceImpl svc = CosmeticsServerRuntime.cosmeticsService();
            CosmeticsNetwork.setServerHandlers(svc);
        } catch (Exception ex) {
            LOGGER.error("CosmeticsServerEvents server started error", ex);
        }
    }

    @net.neoforged.bus.api.SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        CosmeticsServerWiring wiring = WIRING;
        if (wiring == null) return;

        long nowMillis = System.currentTimeMillis();
        wiring.tierService().refreshOnLogin(player.getServer(), player, nowMillis);

        wiring.cosmeticService().syncPlayer(player);
        wiring.cosmeticService().syncCatalogToPlayer(player);

        for (ServerPlayer other : player.getServer().getPlayerList().getPlayers()) {
            if (other == player) continue;
            if (other.level() != player.level()) continue;
            wiring.cosmeticService().syncToViewer(other, player);
            wiring.cosmeticService().syncToViewer(player, other);
        }
    }

    @net.neoforged.bus.api.SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (!(event.getEntity() instanceof ServerPlayer viewer)) return;
        if (!(event.getTarget() instanceof ServerPlayer target)) return;

        CosmeticsServerWiring wiring = WIRING;
        if (wiring == null) return;

        wiring.cosmeticService().syncToViewer(target, viewer);
    }

    @net.neoforged.bus.api.SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!(event.getOriginal() instanceof ServerPlayer original)) return;

        PlayerTierAttachment srcTier = PlayerTierStorage.get(original);
        PlayerTierAttachment dstTier = PlayerTierStorage.get(player);
        if (srcTier != null && dstTier != null) {
            dstTier.deserializeNBT(null, srcTier.serializeNBT(null));
        }

        PlayerCosmeticsAttachment srcCos = PlayerCosmeticsStorage.get(original);
        PlayerCosmeticsAttachment dstCos = PlayerCosmeticsStorage.get(player);
        if (srcCos != null && dstCos != null) {
            dstCos.deserializeNBT(null, srcCos.serializeNBT(null));
        }

        CosmeticsServerWiring wiring = WIRING;
        if (wiring != null) {
            wiring.cosmeticService().syncPlayer(player);
            wiring.cosmeticService().syncCatalogToPlayer(player);
        }
    }

    @net.neoforged.bus.api.SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        CosmeticsServerWiring wiring = WIRING;
        if (wiring == null) return;
        long nowMillis = System.currentTimeMillis();
        wiring.tierService().tick(event.getServer(), nowMillis);
    }
}