package com.pgalaxyp.fragmento.combat.damageModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;

public record DamageRequest(ActorId sourceActorId, ActorId targetActorId, DamageSpec spec) {

    public DamageRequest {
        if (sourceActorId == null || targetActorId == null || spec == null) {
            throw new IllegalArgumentException();
        }
    }
}