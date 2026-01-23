package com.pgalaxyp.fragmento.combat.actor.event;

import com.pgalaxyp.fragmento.combat.actor.api.ActorId;
import com.pgalaxyp.fragmento.combat.actor.api.ActorState;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.Objects;

public record ActorUpserted(ActorId actorId, ActorState state) implements DomainEvent {
    public ActorUpserted {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(state);
    }
}