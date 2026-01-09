package com.pgalaxyp.fragmento.rpg.core.event.event;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;

public record HomingMagicVisualEvent(
        ActorId sourceActorId,
        ActorId targetActorId
) implements VisualEvent {
    public HomingMagicVisualEvent {
        if (sourceActorId == null || targetActorId == null) {
            throw new IllegalArgumentException();
        }
    }
}