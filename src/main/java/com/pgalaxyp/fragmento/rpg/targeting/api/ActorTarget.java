package com.pgalaxyp.fragmento.rpg.targeting.api;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;

public record ActorTarget(ActorId actorId) implements Target {

    public ActorTarget {
        if (actorId == null) {
            throw new IllegalArgumentException();
        }
    }
}