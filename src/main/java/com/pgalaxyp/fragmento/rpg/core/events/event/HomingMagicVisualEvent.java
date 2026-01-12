package com.pgalaxyp.fragmento.rpg.core.events.event;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.QueryId;

public record HomingMagicVisualEvent(
        long frameId,
        int localIndex,
        QueryId queryId,
        ActorId sourceActorId,
        ActorId targetActorId,
        int lifetimeFrames
) implements VisualEvent {
    public HomingMagicVisualEvent {
        if (frameId < 0 || localIndex < 0) {
            throw new IllegalArgumentException();
        }
        if (queryId == null || sourceActorId == null || targetActorId == null) {
            throw new IllegalArgumentException();
        }
        if (lifetimeFrames <= 0) {
            throw new IllegalArgumentException();
        }
    }
}