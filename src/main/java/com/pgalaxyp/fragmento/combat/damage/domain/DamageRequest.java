package com.pgalaxyp.fragmento.combat.damage.domain;

import com.pgalaxyp.fragmento.combat.actor.ActorId;

public record DamageRequest(ActorId sourceActorId, ActorId targetActorId, DamageSpec spec) {

    public DamageRequest {
        if (sourceActorId == null || targetActorId == null || spec == null) {
            throw new IllegalArgumentException();
        }
    }
}