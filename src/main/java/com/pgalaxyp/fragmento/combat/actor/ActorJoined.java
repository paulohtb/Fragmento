package com.pgalaxyp.fragmento.combat.actor;

import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.Objects;

public record ActorJoined(ActorId actorId) implements DomainEvent {
    public ActorJoined { Objects.requireNonNull(actorId); }
}
