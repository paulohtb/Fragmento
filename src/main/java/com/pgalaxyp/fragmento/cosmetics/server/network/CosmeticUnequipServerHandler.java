package com.pgalaxyp.fragmento.cosmetics.server.network;

import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticUnequipRequestPacket;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticServices;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class CosmeticUnequipServerHandler {

    private CosmeticUnequipServerHandler() {}

    public static void handle(CosmeticUnequipRequestPacket pkt, IPayloadContext ctx) {
        ServerPlayer sp = (ServerPlayer) ctx.player();
        CosmeticServices.service()
                .unequipBase(sp.getUUID(), pkt.slot());
    }
}