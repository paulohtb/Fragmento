//package com.pgalaxyp.fragmento.combat.old.network.c2s;
//
//import com.pgalaxyp.fragmento.combat.old.system.skill.SkillAction;
//import com.pgalaxyp.fragmento.combat.old.system.skill.SkillRouter;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.server.level.ServerPlayer;
//import net.neoforged.neoforge.network.handling.IPayloadContext;
//
//public final class SkillC2SPacketHandlers {
//
//    private SkillC2SPacketHandlers() {
//    }
//
//    public static void handleIntent(SkillIntentPacket packet, IPayloadContext ctx) {
//        ctx.enqueueWork(() -> {
//            Player p = ctx.player();
//            if (p instanceof ServerPlayer player) {
//                SkillRouter.handlePacket(
//                        player,
//                        SkillAction.START,
//                        packet.slotId(),
//                        packet.targetId()
//                );
//            }
//        });
//    }
//
//    public static void handleCancel(SkillCancelPacket packet, IPayloadContext ctx) {
//        ctx.enqueueWork(() -> {
//            Player p = ctx.player();
//            if (p instanceof ServerPlayer player) {
//                SkillRouter.handlePacket(
//                        player,
//                        SkillAction.CANCEL,
//                        packet.slotId(),
//                        0
//                );
//            }
//        });
//    }
//}