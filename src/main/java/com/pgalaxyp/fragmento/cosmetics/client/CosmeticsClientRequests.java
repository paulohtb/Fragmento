package com.pgalaxyp.fragmento.cosmetics.client;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.network.c2s.C2SRequestClearBaseCosmeticPayload;
import com.pgalaxyp.fragmento.cosmetics.network.c2s.C2SRequestSetBaseCosmeticPayload;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

public final class CosmeticsClientRequests {

    private static final Logger LOGGER = LogUtils.getLogger();

    private CosmeticsClientRequests() {
    }

    public static void requestSetBase(CosmeticSlot slot, CosmeticId cosmeticId) {
        if (slot == null) return;
        if (cosmeticId == null) return;
        PacketDistributor.sendToServer(new C2SRequestSetBaseCosmeticPayload(slot.ordinal(), cosmeticId.value()));
    }

    public static void requestClearBase(CosmeticSlot slot) {
        if (slot == null) return;
        PacketDistributor.sendToServer(new C2SRequestClearBaseCosmeticPayload(slot.ordinal()));
    }
}