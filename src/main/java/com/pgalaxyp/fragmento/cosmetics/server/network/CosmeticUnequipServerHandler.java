package com.pgalaxyp.fragmento.cosmetics.server.network;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticServices;
import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticUnequipRequestPacket;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticService;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class CosmeticUnequipServerHandler {

    private CosmeticUnequipServerHandler() {}

    public static void handle(CosmeticUnequipRequestPacket pkt, IPayloadContext ctx) {
        if (pkt == null || ctx == null) {
            return;
        }

        if (pkt.slot() == null || pkt.slot() == CosmeticSlot.UNKNOWN) {
            return;
        }

        Object p = ctx.player();
        if (!(p instanceof ServerPlayer sp)) {
            return;
        }

        CosmeticService service = CosmeticServices.service();
        if (service == null) {
            return;
        }

        service.unequipBase(sp.getUUID(), pkt.slot());
    }
}