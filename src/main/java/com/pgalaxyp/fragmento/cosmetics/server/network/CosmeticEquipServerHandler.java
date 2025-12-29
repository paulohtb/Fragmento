package com.pgalaxyp.fragmento.cosmetics.server.network;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticServices;
import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticEquipRequestPacket;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticService;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class CosmeticEquipServerHandler {

    private CosmeticEquipServerHandler() {}

    public static void handle(CosmeticEquipRequestPacket pkt, IPayloadContext ctx) {
        if (pkt == null || ctx == null) {
            return;
        }

        if (pkt.slot() == null || pkt.slot() == CosmeticSlot.UNKNOWN) {
            return;
        }

        if (pkt.cosmeticId() == null) {
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

        service.equipBase(sp.getUUID(), pkt.slot(), pkt.cosmeticId());
    }
}