package com.pgalaxyp.fragmento.tiers.server.sync;

import com.pgalaxyp.fragmento.tiers.network.TierNetwork;
import com.pgalaxyp.fragmento.tiers.network.TierSyncPacket;
import com.pgalaxyp.fragmento.tiers.server.service.TierService;
import com.pgalaxyp.fragmento.tiers.server.service.TierSnapshot;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

@EventBusSubscriber(modid = "fragmento")
public final class TierServerSync {

    private static final long INTERVAL_MILLIS = 10000L;

    private static volatile long lastRunMillis;

    private TierServerSync() {}

    @SubscribeEvent
    public static void onLogin(final PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;

        TierService service = TierNetwork.service();
        if (service == null) return;

        long now = System.currentTimeMillis();
        TierSnapshot snap = service.snapshot(sp.getUUID(), now);
        if (snap == null) return;

        PacketDistributor.sendToPlayer(sp, new TierSyncPacket(snap.tier(), snap.version()));
    }

    @SubscribeEvent
    public static void onServerTick(final ServerTickEvent.Post event) {
        TierService service = TierNetwork.service();
        if (service == null) return;

        long now = System.currentTimeMillis();
        long last = lastRunMillis;

        if (last != 0L) {
            long delta = Math.addExact(now, Math.negateExact(last));
            if (delta < INTERVAL_MILLIS) return;
        }

        lastRunMillis = now;

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        for (ServerPlayer sp : server.getPlayerList().getPlayers()) {
            UUID id = sp.getUUID();
            TierSnapshot snap = service.snapshot(id, now);
            if (snap == null) continue;
            PacketDistributor.sendToPlayer(sp, new TierSyncPacket(snap.tier(), snap.version()));
        }
    }
}