package com.pgalaxyp.fragmento.rpg.core.domain.event;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.effect.EffectDef;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingRequest;

public record TargetingRequestedEvent(
        long actorId,
        ActionId actionId,
        int comboIndex,
        String stepId,
        EffectDef effect,
        TargetingRequest targeting,
        long createdAt
) implements RpgEvent {}