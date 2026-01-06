package com.pgalaxyp.fragmento.rpg.core.rule.command;

import com.pgalaxyp.fragmento.rpg.core.domain.effect.EffectId;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.Target;

public record ApplyEffect(
        long actorId,
        EffectId effect,
        Target target,
        long createdAt
) implements RuleCommand {}