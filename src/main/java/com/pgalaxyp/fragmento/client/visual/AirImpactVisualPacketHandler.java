package com.pgalaxyp.fragmento.client.visual;

import com.pgalaxyp.fragmento.network.s2c.SpiritImpactVisualPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class AirImpactVisualPacketHandler {

    private AirImpactVisualPacketHandler() {
    }

    public static void handle(SpiritImpactVisualPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;

            Vec3 pos = packet.pos();
            AirImpactVisualManager.spawn(pos);
        });
    }
}