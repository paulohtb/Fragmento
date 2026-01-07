package com.pgalaxyp.fragmento.rpg.core.rule.action;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.effect.EffectId;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingRequest;

public record PendingTargeting(
        long actorId,
        ActionId actionId,
        int comboIndex,
        EffectId effect,
        TargetingRequest request,
        double maxEntityDistance,
        double maxVirtualDistance,
        long createdAt
) {}