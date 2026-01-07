package com.pgalaxyp.fragmento.rpg.core.domain.event;

import com.pgalaxyp.fragmento.rpg.core.domain.effect.EffectId;

public record EffectTriggered(
        long actorId,
        EffectId effectId
) implements DomainEvent {}