package com.pgalaxyp.fragmento.rpg.engine.catalyst;

import com.pgalaxyp.fragmento.rpg.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.effect.RpgEffect;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public interface CatalystEffectAdapter {

    List<RpgEffect> onAction(
            ServerPlayer player,
            ActionKind action,
            int comboStep,
            SkillId skillId,
            Time now
    );
}