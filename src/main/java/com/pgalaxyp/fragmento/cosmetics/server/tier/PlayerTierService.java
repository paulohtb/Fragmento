package com.pgalaxyp.fragmento.cosmetics.server.tier;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTier;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2CSyncTierPayload;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2CToastPayload;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public final class PlayerTierService {

    public static final long COMMAND_COOLDOWN_MILLIS = 60000L;
    public static final long SYNC_INTERVAL_MILLIS = 900000L;
    public static final long FAIL_OPEN_WINDOW_MILLIS = 43200000L;

    private static final int SCAN_PLAYERS_PER_TICK = 4;

    private final TierApiClient api;
    private final PlayerTierUpdateListener listener;
    private final ConcurrentHashMap<UUID, CompletableFuture<?>> inflight;

    private volatile int scanIndex;

    public PlayerTierService(TierApiClient api, PlayerTierUpdateListener listener) {
        this.api = api;
        this.listener = listener;
        this.inflight = new ConcurrentHashMap<>();
        this.scanIndex = 0;
    }

    public CosmeticTier tier(ServerPlayer player) {
        PlayerTierAttachment att = PlayerTierStorage.get(player);
        return att.tier();
    }

    public boolean consumeLinkCooldown(ServerPlayer player, long nowMillis) {
        PlayerTierAttachment att = PlayerTierStorage.get(player);
        return att.consumeCommandCooldown(nowMillis, COMMAND_COOLDOWN_MILLIS);
    }

    public void refreshOnLogin(MinecraftServer server, ServerPlayer player, long nowMillis) {
        UUID id = player.getUUID();
        beginFetch(server, player, nowMillis, id, () -> api.fetchTier(id));
    }

    public void tick(MinecraftServer server, long nowMillis) {
        java.util.List<ServerPlayer> players = server.getPlayerList().getPlayers();
        int size = players.size();
        if (size == 0) {
            scanIndex = 0;
            return;
        }

        int idx = scanIndex;
        for (int i = 0; i < SCAN_PLAYERS_PER_TICK; i++) {
            if (idx >= size) idx = 0;
            ServerPlayer player = players.get(idx);
            idx++;
            if (player == null) continue;
            if (player.isRemoved()) continue;
            periodicRefresh(server, player, nowMillis);
            enforceFailOpen(server, player, nowMillis);
        }
        scanIndex = idx;
    }

    private void periodicRefresh(MinecraftServer server, ServerPlayer player, long nowMillis) {
        PlayerTierAttachment att = PlayerTierStorage.get(player);
        long lastAttempt = att.lastAttemptMillis();
        long due = lastAttempt + SYNC_INTERVAL_MILLIS;
        if (nowMillis < due) return;

        UUID id = player.getUUID();
        if (isInflight(id)) return;

        att.touchAttempt(nowMillis);
        beginFetch(server, player, nowMillis, id, () -> api.fetchTier(id));
    }

    public void linkKey(MinecraftServer server, ServerPlayer player, String key, long nowMillis) {
        UUID id = player.getUUID();
        beginFetch(server, player, nowMillis, id, () -> api.bindKey(id, key));
    }

    private boolean isInflight(UUID id) {
        CompletableFuture<?> existing = inflight.get(id);
        return existing != null && !existing.isDone();
    }

    private void beginFetch(MinecraftServer server, ServerPlayer player, long nowMillis, UUID id, Supplier<CompletableFuture<Optional<CosmeticTier>>> call) {
        if (isInflight(id)) return;

        CompletableFuture<Optional<CosmeticTier>> future = call.get();
        if (future == null) return;

        CompletableFuture<?> existing = inflight.putIfAbsent(id, future);
        if (existing != null && !existing.isDone()) {
            return;
        }

        future.whenComplete(new BiConsumer<Optional<CosmeticTier>, Throwable>() {
            @Override
            public void accept(Optional<CosmeticTier> opt, Throwable ex) {
                inflight.remove(id, future);
                if (ex != null) return;
                Optional<CosmeticTier> safe = opt == null ? Optional.empty() : opt;
                applyTier(server, player, nowMillis, safe);
            }
        });
    }

    private void applyTier(MinecraftServer server, ServerPlayer player, long nowMillis, Optional<CosmeticTier> opt) {
        server.execute(new Runnable() {
            @Override
            public void run() {
                if (player.isRemoved()) return;

                CosmeticTier next = opt.orElse(CosmeticTier.TIER_0);
                PlayerTierAttachment att = PlayerTierStorage.get(player);

                boolean changed = att.applyTier(next, nowMillis);

                PacketDistributor.sendToPlayer(player, S2CSyncTierPayload.of(next, att.version()));

                if (changed) {
                    PacketDistributor.sendToPlayer(player, new S2CToastPayload("Tier", "Tier updated"));
                    if (listener != null) listener.onTierApplied(server, player, next);
                }
            }
        });
    }

    private void enforceFailOpen(MinecraftServer server, ServerPlayer player, long nowMillis) {
        PlayerTierAttachment att = PlayerTierStorage.get(player);
        long lastSuccess = att.lastSuccessSyncMillis();
        if (lastSuccess == 0L) return;

        long deadline = lastSuccess + FAIL_OPEN_WINDOW_MILLIS;
        if (nowMillis < deadline) return;

        if (att.tier() == CosmeticTier.TIER_0) return;

        boolean changed = att.applyTier(CosmeticTier.TIER_0, nowMillis);
        if (!changed) return;

        PacketDistributor.sendToPlayer(player, S2CSyncTierPayload.of(CosmeticTier.TIER_0, att.version()));
        PacketDistributor.sendToPlayer(player, new S2CToastPayload("Tier", "Tier expired"));
        if (listener != null) listener.onTierApplied(server, player, CosmeticTier.TIER_0);
    }
}