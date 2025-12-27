package com.pgalaxyp.fragmento.cosmetics.network;

import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticService;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class CosmeticEquipServerHandler {

    private CosmeticEquipServerHandler() {}

    public static void handle(CosmeticEquipRequestPacket pkt, IPayloadContext ctx) {
        Object p = ctx.player();
        if (!(p instanceof ServerPlayer sp)) return;

        CosmeticService service = CosmeticNetwork.service();
        if (service == null) return;

        service.equipBase(sp.getUUID(), pkt.slot(), pkt.cosmeticId());
    }
}