package com.pgalaxyp.fragmento.combat.actorModule.event;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorState;
import com.pgalaxyp.fragmento.combat.random.FrameEvent;

import java.util.Objects;

public record ActorUpserted(ActorId actorId, ActorState state) implements FrameEvent {
    public ActorUpserted {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(state);
    }
}