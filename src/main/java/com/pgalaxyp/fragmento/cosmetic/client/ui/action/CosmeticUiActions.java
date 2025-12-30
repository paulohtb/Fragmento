package com.pgalaxyp.fragmento.cosmetic.client.ui.action;

import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetic.common.network.CosmeticEquipRequestPacket;
import com.pgalaxyp.fragmento.cosmetic.common.network.CosmeticUnequipRequestPacket;
import net.neoforged.neoforge.network.PacketDistributor;

public final class CosmeticUiActions {

    private CosmeticUiActions() {}

    public static void equip(CosmeticId id) {
        PacketDistributor.sendToServer(new CosmeticEquipRequestPacket(id));
    }

    public static void unequip(CosmeticSlot slot) {
        PacketDistributor.sendToServer(new CosmeticUnequipRequestPacket(slot));
    }
}