package com.pgalaxyp.fragmento.rpg.core.domain.event;

import com.pgalaxyp.fragmento.rpg.core.domain.effect.EffectSpec;

public record EffectTriggered(
        long actorId,
        EffectSpec effect,
        long requestId
) implements DomainEvent {
    public EffectTriggered {
        if (effect == null) throw new IllegalArgumentException("EffectTriggered.effect");
        requestId = Math.max(0L, requestId);
    }
}