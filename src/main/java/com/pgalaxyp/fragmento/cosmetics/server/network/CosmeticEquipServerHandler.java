package com.pgalaxyp.fragmento.cosmetics.server.network;

import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticEquipRequestPacket;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticServices;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class CosmeticEquipServerHandler {

    private CosmeticEquipServerHandler() {}

    public static void handle(CosmeticEquipRequestPacket pkt, IPayloadContext ctx) {
        ServerPlayer sp = (ServerPlayer) ctx.player();
        CosmeticServices.service()
                .equipBase(sp.getUUID(), pkt.slot(), pkt.cosmeticId());
    }
}