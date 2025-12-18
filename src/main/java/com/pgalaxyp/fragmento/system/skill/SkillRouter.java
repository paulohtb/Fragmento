package com.pgalaxyp.fragmento.system.skill;

import com.pgalaxyp.fragmento.system.channel.ChannelingService;
import com.pgalaxyp.fragmento.content.bard.gameplay.BardCatalystCooldownService;
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

        if (BardCatalystCooldownService.isOnCooldown(player, stack.getItem())) return;

        if (slot == SkillSlot.SPECIAL && ChannelingService.hasActive(player)) return;

        SkillDispatchService.dispatch(
                player,
                slot,
                SkillAction.START,
                targetId
        );
    }
}