package com.pgalaxyp.fragmento.rpg_old.engine.catalyst;

import com.pgalaxyp.fragmento.rpg_old.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg_old.effect.RpgEffect;
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