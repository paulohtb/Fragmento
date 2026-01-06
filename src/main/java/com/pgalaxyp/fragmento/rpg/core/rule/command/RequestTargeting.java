package com.pgalaxyp.fragmento.rpg.core.rule.command;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.effect.EffectDef;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingRequest;

public record RequestTargeting(
        long actorId,
        ActionId actionId,
        int comboIndex,
        String stepId,
        EffectDef effect,
        TargetingRequest targeting,
        double maxEntityDistanceBlocks,
        double maxVirtualDistanceBlocks,
        long createdAt
) implements RuleCommand {}