package com.pgalaxyp.fragmento.combat.ability.event;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import com.pgalaxyp.fragmento.combat.actor.api.ActorId;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.Objects;

public record AbilityResolved(ActorId actorId, AbilityId abilityId) implements DomainEvent {
    public AbilityResolved {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(abilityId);
    }
}