package com.pgalaxyp.fragmento.cosmetic.server.network;

import com.pgalaxyp.fragmento.cosmetic.common.network.CosmeticEquipRequestPacket;
import com.pgalaxyp.fragmento.cosmetic.server.service.CosmeticServices;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class CosmeticEquipServerHandler {

    private CosmeticEquipServerHandler() {}

    public static void handle(CosmeticEquipRequestPacket pkt, IPayloadContext ctx) {
        if (pkt == null || ctx == null) return;

        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer sp)) return;
            if (CosmeticServices.service() == null) return;
            CosmeticServices.service().equip(sp.getUUID(), pkt.cosmeticId());
        });
    }
}