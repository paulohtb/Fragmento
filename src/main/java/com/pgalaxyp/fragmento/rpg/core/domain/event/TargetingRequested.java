package com.pgalaxyp.fragmento.rpg.core.domain.event;

import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingId;

public record TargetingRequested(
        long actorId,
        TargetingId targetingId
) implements DomainEvent {}