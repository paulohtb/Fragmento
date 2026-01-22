package com.pgalaxyp.fragmento.combat.ability.event;

import com.pgalaxyp.fragmento.combat.ability.api.AbilitySnapshot;
import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.Objects;

public record AbilityEnded(ActorId actorId, AbilitySnapshot snapshot) implements DomainEvent {
    public AbilityEnded {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(snapshot);
    }
}