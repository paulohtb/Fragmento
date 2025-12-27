package com.pgalaxyp.fragmento.cosmetics.network;

import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticService;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class CosmeticUnequipServerHandler {

    private CosmeticUnequipServerHandler() {}

    public static void handle(CosmeticUnequipRequestPacket pkt, IPayloadContext ctx) {
        Object p = ctx.player();
        if (!(p instanceof ServerPlayer sp)) return;

        CosmeticService service = CosmeticNetwork.service();
        if (service == null) return;

        service.unequipBase(sp.getUUID(), pkt.slot());
    }
}