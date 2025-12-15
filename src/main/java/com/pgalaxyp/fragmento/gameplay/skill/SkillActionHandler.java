package com.pgalaxyp.fragmento.gameplay.skill;

import net.minecraft.server.level.ServerPlayer;

public interface SkillActionHandler {

    void handle(
            ServerPlayer player,
            SkillSlot slot,
            SkillAction action,
            int targetId
    );
}
