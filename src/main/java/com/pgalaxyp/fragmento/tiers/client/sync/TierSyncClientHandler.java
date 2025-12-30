package com.pgalaxyp.fragmento.tiers.client.sync;

import com.pgalaxyp.fragmento.tiers.client.state.TierClientState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class TierSyncClientHandler {

    private TierSyncClientHandler() {}

    public static void handle(TierLevelSyncPacket pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() ->
                TierClientState.update(
                        pkt.level(),
                        pkt.version()
                )
        );
    }
}