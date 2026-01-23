package com.pgalaxyp.fragmento.combat.actor.event;

import com.pgalaxyp.fragmento.combat.actor.api.ActorId;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.Objects;

public record ActorRemoved(ActorId actorId) implements DomainEvent {
    public ActorRemoved {
        Objects.requireNonNull(actorId);
    }
}