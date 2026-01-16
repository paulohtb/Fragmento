package com.pgalaxyp.fragmento.combat.event;

import com.pgalaxyp.fragmento.combat.core.ids.*;

public record HomingMagicVisualEvent(
        long frameId,
        int localIndex,
        ActorId sourceActorId,
        ActorId targetActorId,
        int lifetimeFrames
) implements DomainEvent {

    public HomingMagicVisualEvent {
        if (frameId < 0 || localIndex < 0) throw new IllegalArgumentException();
        if (sourceActorId == null || targetActorId == null) throw new IllegalArgumentException();
        if (lifetimeFrames <= 0) throw new IllegalArgumentException();
    }
}