package com.pgalaxyp.fragmento.rpg.core.event.resolution;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;

public record TargetingCandidate(
        ActorId actorId,
        int distanceSquared
) {
    public TargetingCandidate {
        if (actorId == null) {
            throw new IllegalArgumentException();
        }
        if (distanceSquared < 0) {
            throw new IllegalArgumentException();
        }
    }
}