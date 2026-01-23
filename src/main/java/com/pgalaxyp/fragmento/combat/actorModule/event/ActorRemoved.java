package com.pgalaxyp.fragmento.combat.actorModule.event;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.random.FrameEvent;

import java.util.Objects;

public record ActorRemoved(ActorId actorId) implements FrameEvent {
    public ActorRemoved {
        Objects.requireNonNull(actorId);
    }
}