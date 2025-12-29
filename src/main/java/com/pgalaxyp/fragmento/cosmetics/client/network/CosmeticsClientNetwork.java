package com.pgalaxyp.fragmento.cosmetics.client.network;

import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticSyncRequestPacket;
import net.neoforged.neoforge.network.PacketDistributor;

public final class CosmeticsClientNetwork {

    private CosmeticsClientNetwork() {}

    public static void requestSync() {
        PacketDistributor.sendToServer(CosmeticSyncRequestPacket.INSTANCE);
    }
}