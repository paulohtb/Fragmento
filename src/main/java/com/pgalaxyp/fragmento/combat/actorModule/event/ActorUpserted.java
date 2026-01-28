package com.pgalaxyp.fragmento.combat.actorModule.event;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import java.util.Objects;

public record ActorUpserted(ActorId actorId, ActorState state) implements FrameEvent {
    public ActorUpserted {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(state);
    }
}