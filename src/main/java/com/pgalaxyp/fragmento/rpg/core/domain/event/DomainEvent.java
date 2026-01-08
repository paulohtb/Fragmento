package com.pgalaxyp.fragmento.rpg.core.domain.event;

import com.pgalaxyp.fragmento.rpg.core.domain.effect.EffectId;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingId;

public record DomainEvent(
        DomainEventType type,
        long actorId,
        EffectId effectId,
        int comboIndex,
        TargetingId targetingId
) {}