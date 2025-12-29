package com.pgalaxyp.fragmento.tiers.common.network;

import com.pgalaxyp.fragmento.tiers.common.view.TierClientState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class TierSyncClientHandler {

    private TierSyncClientHandler() {}

    public static void handle(TierSyncPacket pkt, IPayloadContext ctx) {
        if (pkt == null || ctx == null) {
            return;
        }
        ctx.enqueueWork(new Apply(pkt));
    }

    private record Apply(TierSyncPacket pkt) implements Runnable {
        @Override
        public void run() {
            TierClientState.update(pkt.tier(), pkt.version());
        }
    }
}