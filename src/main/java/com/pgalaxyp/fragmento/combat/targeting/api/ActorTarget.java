package com.pgalaxyp.fragmento.combat.targeting.api;

import com.pgalaxyp.fragmento.combat.core.ids.*;

public record ActorTarget(ActorId actorId) implements Target {

    public ActorTarget {
        if (actorId == null) {
            throw new IllegalArgumentException();
        }
    }
}