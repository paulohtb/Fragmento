package com.pgalaxyp.fragmento.combat.targetingModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import java.util.Objects;

public record TargetingRequest(ActorId casterId, TargetingSpec spec) {
    public TargetingRequest {
        Objects.requireNonNull(casterId);
        Objects.requireNonNull(spec);
    }
}