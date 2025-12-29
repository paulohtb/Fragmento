package com.pgalaxyp.fragmento.tiers.common.network;

import com.pgalaxyp.fragmento.tiers.common.view.TierClientState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class TierSyncClientHandler {

    private TierSyncClientHandler() {}

    public static void handle(TierLevelSyncPacket pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() ->
                TierClientState.update(pkt.level(), pkt.version())
        );
    }
}