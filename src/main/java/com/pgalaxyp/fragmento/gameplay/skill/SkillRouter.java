package com.pgalaxyp.fragmento.gameplay.skill;

import com.pgalaxyp.fragmento.content.bard.gameplay.BardCatalystCooldownService;
import com.pgalaxyp.fragmento.gameplay.channel.ChannelingService;
import com.pgalaxyp.fragmento.platform.network.packet.SkillPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public final class SkillRouter {

    private SkillRouter() {
    }

    public static void handlePacket(ServerPlayer player, SkillPacket packet) {
        SkillSlot slot = SkillSlot.fromId(packet.slotId());
        if (slot == null) return;

        SkillAction action = mapAction(packet.action());
        if (action == null) return;

        if (!SkillRateLimitService.allow(player, slot, action)) return;

        if (action == SkillAction.START) {
            ItemStack stack = player.getMainHandItem();
            if (stack.isEmpty()) return;

            if (BardCatalystCooldownService.isOnCooldown(player, stack.getItem())) return;

            if (slot == SkillSlot.SPECIAL && ChannelingService.hasActive(player)) return;
        }

        SkillDispatchService.dispatch(
                player,
                slot,
                action,
                packet.targetId()
        );
    }

    private static SkillAction mapAction(SkillPacket.Action action) {
        if (action == null) return null;
        return switch (action) {
            case START -> SkillAction.START;
            case CANCEL -> SkillAction.CANCEL;
        };
    }
}