package com.pgalaxyp.fragmento.gameplay.skill;

import com.pgalaxyp.fragmento.gameplay.channel.ChannelingService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public final class SkillDispatchService {

    private SkillDispatchService() {
    }

    public static void dispatch(
            ServerPlayer player,
            SkillSlot slot,
            SkillAction action,
            int targetId
    ) {
        ItemStack stack = player.getMainHandItem();
        SkillActionHandler handler = SkillActionRegistry.resolve(stack);

        if (handler != null) {
            handler.handle(player, slot, action, targetId);
            return;
        }

        if (action == SkillAction.CANCEL) {
            ChannelingService.cancel(player);
        }
    }
}
