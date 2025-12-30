package com.pgalaxyp.fragmento.cosmetics.server.network;

import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticUnequipRequestPacket;
import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticServices;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class CosmeticUnequipServerHandler {

    private CosmeticUnequipServerHandler() {}

    public static void handle(CosmeticUnequipRequestPacket pkt, IPayloadContext ctx) {
        if (pkt == null || ctx == null) return;

        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer sp)) return;
            if (CosmeticServices.service() == null) return;
            CosmeticServices.service().unequip(sp.getUUID(), pkt.slot());
        });
    }
}