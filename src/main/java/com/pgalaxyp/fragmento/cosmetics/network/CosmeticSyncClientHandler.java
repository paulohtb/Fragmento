package com.pgalaxyp.fragmento.cosmetics.network;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadoutSnapshot;
import com.pgalaxyp.fragmento.cosmetics.client.CosmeticsClientState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class CosmeticSyncClientHandler {

    private CosmeticSyncClientHandler() {}

    public static void handle(final CosmeticSyncPacket pkt, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            CosmeticsClientState.put(
                    pkt.playerId(),
                    new CosmeticLoadoutSnapshot(pkt.loadout(), pkt.version())
            );
        });
    }
}