package com.pgalaxyp.fragmento.cosmetics.client.ui.action;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticEquipRequestPacket;
import com.pgalaxyp.fragmento.cosmetics.common.network.CosmeticUnequipRequestPacket;
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