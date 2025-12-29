package com.pgalaxyp.fragmento.cosmetics.server.network;

import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticServices;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadoutSnapshot;
import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticSyncPacket;
import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticSyncRequestPacket;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticService;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class CosmeticSyncServerHandler {

    private static final long MIN_INTERVAL_MILLIS = 2000L;
    private static final ConcurrentHashMap<UUID, Long> LAST = new ConcurrentHashMap<>();
    private static final double INITIAL_SYNC_RANGE_SQ = 128.0 * 128.0;

    private CosmeticSyncServerHandler() {}

    public static void handle(CosmeticSyncRequestPacket pkt, IPayloadContext ctx) {
        CosmeticService service = CosmeticServices.service();
        if (service == null) {
            return;
        }

        Object p = ctx.player();
        if (!(p instanceof ServerPlayer requester)) {
            return;
        }

        UUID requesterId = requester.getUUID();
        long now = System.currentTimeMillis();
        Long last = LAST.get(requesterId);
        if (last != null && now < last + MIN_INTERVAL_MILLIS) {
            return;
        }
        LAST.put(requesterId, now);

        ctx.enqueueWork(() -> {
            CosmeticLoadoutSnapshot selfSnap = service.getSnapshot(requesterId);
            if (selfSnap != null) {
                PacketDistributor.sendToPlayer(
                        requester,
                        new CosmeticSyncPacket(requesterId, selfSnap.loadout(), selfSnap.version())
                );
            }

            int count = requester.serverLevel().players().size();
            for (int i = 0; i < count; i++) {
                ServerPlayer target = requester.serverLevel().players().get(i);
                if (target == requester) {
                    continue;
                }

                double dx = sub(requester.getX(), target.getX());
                double dy = sub(requester.getY(), target.getY());
                double dz = sub(requester.getZ(), target.getZ());
                double distSq = dx * dx + dy * dy + dz * dz;

                if (distSq > INITIAL_SYNC_RANGE_SQ) {
                    continue;
                }

                UUID targetId = target.getUUID();
                CosmeticLoadoutSnapshot snap = service.getSnapshot(targetId);
                if (snap == null) {
                    continue;
                }

                PacketDistributor.sendToPlayer(
                        requester,
                        new CosmeticSyncPacket(targetId, snap.loadout(), snap.version())
                );
            }
        });
    }

    private static double sub(double a, double b) {
        return a + neg(b);
    }

    private static double neg(double v) {
        return Double.longBitsToDouble(Double.doubleToRawLongBits(v) ^ 0x8000000000000000L);
    }
}