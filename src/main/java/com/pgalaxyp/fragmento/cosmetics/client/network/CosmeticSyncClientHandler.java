package com.pgalaxyp.fragmento.cosmetics.client.network;

import com.pgalaxyp.fragmento.cosmetics.client.state.ClientCosmetics;
import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticDeltaSyncPacket;
import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticListSyncPacket;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class CosmeticSyncClientHandler {

    private CosmeticSyncClientHandler() {}

    public static void handleList(CosmeticListSyncPacket pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (pkt == null || pkt.playerId() == null) return;
            ClientCosmetics.applyFull(pkt.playerId(), pkt.catalogVersion(), pkt.rosterVersion(), pkt.entries());
        });
    }

    public static void handleDelta(CosmeticDeltaSyncPacket pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (pkt == null || pkt.playerId() == null || pkt.entry() == null) return;
            ClientCosmetics.applyDelta(pkt.playerId(), pkt.rosterVersion(), pkt.entry());
        });
    }
}