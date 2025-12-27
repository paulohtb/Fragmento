package com.pgalaxyp.fragmento.cosmetics.client.lifecycle;

import net.neoforged.neoforge.network.PacketDistributor;
import com.pgalaxyp.fragmento.cosmetics.network.CosmeticSyncRequestPacket;

public final class CosmeticsClientNetwork {

    private CosmeticsClientNetwork() {}

    public static void requestSync() {
        PacketDistributor.sendToServer(CosmeticSyncRequestPacket.INSTANCE);
    }
}