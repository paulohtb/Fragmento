//package com.pgalaxyp.fragmento.combat.old.client.visual;
//
//import net.minecraft.client.Minecraft;
//import net.neoforged.neoforge.network.handling.IPayloadContext;
//
//public final class MinorWindVortexVisualPacketHandler {
//
//    private MinorWindVortexVisualPacketHandler() {}
//
////    public static void handle(MinorWindVortexVisualPacket packet, IPayloadContext ctx) {
////        ctx.enqueueWork(() -> {
////            Minecraft mc = Minecraft.getInstance();
////            if (mc.level == null) return;
////
////            MinorWindVortexVisualManager.spawn(
////                    packet.pos(),
////                    packet.loopDuration(),
////                    packet.gapDuration(),
////                    packet.loops(),
////                    packet.sizeXZ()
////            );
////        });
////    }
//}