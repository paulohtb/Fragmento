package com.pgalaxyp.fragmento.content.bard.skill;

import com.pgalaxyp.fragmento.system.skill.BardSkillServerController;
import com.pgalaxyp.fragmento.system.channel.ChannelingService;
import com.pgalaxyp.fragmento.system.skill.SkillAction;
import com.pgalaxyp.fragmento.system.skill.SkillActionHandler;
import com.pgalaxyp.fragmento.system.skill.SkillSlot;
import net.minecraft.server.level.ServerPlayer;

public final class BardSkillActionHandler implements SkillActionHandler {

    @Override
    public void handle(ServerPlayer player, SkillSlot slot, SkillAction action, int targetId) {
        if (action == SkillAction.CANCEL) {
            ChannelingService.cancel(player);
            return;
        }

        if (action == SkillAction.START) {
            BardSkillServerController.handleStart(player, slot, targetId);
        }
    }
}
