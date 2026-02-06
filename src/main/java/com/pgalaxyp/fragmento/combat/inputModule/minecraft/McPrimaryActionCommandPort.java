package com.pgalaxyp.fragmento.combat.inputModule.minecraft;

import com.pgalaxyp.fragmento.combat.inputModule.port.PrimaryActionCommandPort;
import com.pgalaxyp.fragmento.combat.networkModule.minecraft.PrimaryActionPayload;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.PacketDistributor;

public final class McPrimaryActionCommandPort implements PrimaryActionCommandPort {
    @Override public boolean sendPrimaryAction() {
        if (Minecraft.getInstance().getConnection() == null) return false;
        PacketDistributor.sendToServer(PrimaryActionPayload.INSTANCE);
        return true;
    }
}