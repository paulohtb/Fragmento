package com.pgalaxyp.fragmento.combat.actor;

import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.Objects;

public record ActorJoinRequested(ActorId actorId) implements DomainEvent {
    public ActorJoinRequested { Objects.requireNonNull(actorId); }
}