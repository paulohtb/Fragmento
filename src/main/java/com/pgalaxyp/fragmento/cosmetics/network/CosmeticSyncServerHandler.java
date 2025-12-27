package com.pgalaxyp.fragmento.cosmetics.network;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadoutSnapshot;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticService;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CosmeticSyncServerHandler {

    private static final long MIN_INTERVAL_MILLIS = 2000L;
    private static final ConcurrentHashMap<UUID, Long> LAST = new ConcurrentHashMap<>();
    private static final double INITIAL_SYNC_RANGE_SQ = 128.0 * 128.0;

    private CosmeticSyncServerHandler() {}

    public static void handle(final CosmeticSyncRequestPacket pkt, final IPayloadContext ctx) {
        final CosmeticService service = CosmeticNetwork.service();
        if (service == null) return;

        final Object p = ctx.player();
        if (!(p instanceof ServerPlayer requester)) return;

        UUID requesterId = requester.getUUID();
        long now = System.currentTimeMillis();
        Long last = LAST.get(requesterId);
        if (last != null && now < last + MIN_INTERVAL_MILLIS) {
            return;
        }
        LAST.put(requesterId, now);

        ctx.enqueueWork(() -> {
            MinecraftServer server = requester.getServer();
            if (server == null) return;

            CosmeticLoadoutSnapshot selfSnap =
                    service.getSnapshot(requester.getUUID());

            if (selfSnap != null) {
                PacketDistributor.sendToPlayer(
                        requester,
                        new CosmeticSyncPacket(
                                requester.getUUID(),
                                selfSnap.loadout(),
                                selfSnap.version()
                        )
                );
            }

            for (ServerPlayer target : requester.serverLevel().players()) {
                if (target == requester) continue;

                double dx = requester.getX() - target.getX();
                double dy = requester.getY() - target.getY();
                double dz = requester.getZ() - target.getZ();
                double distSq = dx * dx + dy * dy + dz * dz;

                if (distSq > INITIAL_SYNC_RANGE_SQ) continue;

                CosmeticLoadoutSnapshot snap =
                        service.getSnapshot(target.getUUID());

                if (snap == null) continue;

                PacketDistributor.sendToPlayer(
                        requester,
                        new CosmeticSyncPacket(
                                target.getUUID(),
                                snap.loadout(),
                                snap.version()
                        )
                );
            }
        });
    }
}