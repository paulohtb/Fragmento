package com.pgalaxyp.fragmento.cosmetics.client.network;

import com.pgalaxyp.fragmento.cosmetics.client.state.CosmeticsClientState;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadoutSnapshot;
import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticSyncPacket;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class CosmeticSyncClientHandler {

    private CosmeticSyncClientHandler() {}

    public static void handle(CosmeticSyncPacket pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() ->
                CosmeticsClientState.put(
                        pkt.playerId(),
                        new CosmeticLoadoutSnapshot(pkt.loadout(), pkt.version())
                )
        );
    }
}