package com.pgalaxyp.fragmento.rpg.core.event.event;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;

public record HomingMagicVisualEvent(
        ActorId sourceActorId,
        ActorId targetActorId,
        int lifetimeFrames
) implements VisualEvent {
    public HomingMagicVisualEvent {
        if (sourceActorId == null || targetActorId == null) {
            throw new IllegalArgumentException();
        }
        if (lifetimeFrames <= 0) {
            throw new IllegalArgumentException();
        }
    }
}