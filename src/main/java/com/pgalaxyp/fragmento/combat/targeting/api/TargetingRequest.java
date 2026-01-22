package com.pgalaxyp.fragmento.combat.targeting.api;

import com.pgalaxyp.fragmento.combat.actor.ActorId;
import java.util.Objects;

public record TargetingRequest(ActorId casterId, TargetingSpec spec) {
    public TargetingRequest {
        Objects.requireNonNull(casterId);
        Objects.requireNonNull(spec);
    }
}