package com.pgalaxyp.fragmento.rpg.core.domain.event;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.EffectId;

public record VisualEffectRequested(
        ActorId actorId,
        EffectId effectId,
        int stepIndex
) implements DomainEvent {}