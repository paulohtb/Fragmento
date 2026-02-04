package com.pgalaxyp.fragmento.combat.damageModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import java.util.Objects;

public record DamageRequest(ActorId sourceActorId, ActorId targetActorId, DamageSpec spec) {
    public DamageRequest {
        Objects.requireNonNull(sourceActorId);
        Objects.requireNonNull(targetActorId);
        Objects.requireNonNull(spec);
    }
}