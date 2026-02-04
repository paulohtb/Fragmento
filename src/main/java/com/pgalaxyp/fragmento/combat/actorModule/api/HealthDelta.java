package com.pgalaxyp.fragmento.combat.actorModule.api;

import java.util.Objects;

public record HealthDelta(ActorId actorId, int deltaHearts) {
    public HealthDelta {
        Objects.requireNonNull(actorId);
        if (deltaHearts == 0) throw new IllegalArgumentException();
    }
}