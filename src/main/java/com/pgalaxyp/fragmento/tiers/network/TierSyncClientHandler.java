package com.pgalaxyp.fragmento.tiers.network;

import com.pgalaxyp.fragmento.tiers.client.TierClientState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class TierSyncClientHandler {

    private TierSyncClientHandler() {}

    public static void handle(final TierSyncPacket pkt, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> TierClientState.update(pkt.tier(), pkt.version()));
    }
}