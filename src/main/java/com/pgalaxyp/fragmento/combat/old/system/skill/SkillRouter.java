package com.pgalaxyp.fragmento.combat.old.system.skill;

import com.pgalaxyp.fragmento.combat.old.system.channel.ChannelingService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public final class SkillRouter {

    private SkillRouter() {
    }

    public static void handlePacket(
            ServerPlayer player,
            SkillAction action,
            int slotId,
            int targetId
    ) {
        if (player == null || action == null) return;

        SkillSlot slot = SkillSlot.fromId(slotId);
        if (slot == null) return;

        if (!SkillRateLimitService.allow(player, slot, action)) return;

        if (action == SkillAction.CANCEL) {
            ChannelingService.cancel(player);
            return;
        }

        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) return;

        if (slot == SkillSlot.SPECIAL && ChannelingService.hasActive(player)) return;

        SkillDispatchService.dispatch(
                player,
                slot,
                SkillAction.START,
                targetId
        );
    }
}