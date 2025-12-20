package com.pgalaxyp.fragmento.client.visual;

import com.pgalaxyp.fragmento.network.s2c.MinorWindVortexVisualPacket;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class MinorWindVortexVisualPacketHandler {

    private MinorWindVortexVisualPacketHandler() {
    }

    public static void handle(MinorWindVortexVisualPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;
            MinorWindVortexVisualManager.spawn(
                    packet.pos(),
                    packet.loopDuration(),
                    packet.gapDuration(),
                    packet.loops()
            );
        });
    }
}