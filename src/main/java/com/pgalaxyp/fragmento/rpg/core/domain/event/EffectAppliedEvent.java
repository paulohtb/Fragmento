package com.pgalaxyp.fragmento.rpg.core.domain.event;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.effect.EffectDef;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.Target;

public record EffectAppliedEvent(
        long actorId,
        ActionId actionId,
        int comboIndex,
        String stepId,
        EffectDef effect,
        Target target,
        long createdAt
) implements RpgEvent {}