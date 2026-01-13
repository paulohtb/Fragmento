package com.pgalaxyp.fragmento.rpg.damage.domain;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;

public record DamageRequest(ActorId sourceActorId, ActorId targetActorId, DamageSpec spec) {

    public DamageRequest {
        if (sourceActorId == null || targetActorId == null || spec == null) {
            throw new IllegalArgumentException();
        }
    }
}