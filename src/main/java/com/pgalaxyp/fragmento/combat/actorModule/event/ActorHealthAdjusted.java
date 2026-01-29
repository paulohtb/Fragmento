package com.pgalaxyp.fragmento.combat.actorModule.event;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import java.util.Objects;

public record ActorHealthAdjusted(ActorId actorId, int deltaHearts) implements FrameEvent {
    public ActorHealthAdjusted {
        Objects.requireNonNull(actorId);
        if (deltaHearts == 0) throw new IllegalArgumentException();
    }
}