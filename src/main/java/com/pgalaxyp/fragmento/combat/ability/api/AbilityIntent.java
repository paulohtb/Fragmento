package com.pgalaxyp.fragmento.combat.ability.api;

import com.pgalaxyp.fragmento.combat.actor.api.ActorId;
import java.util.Objects;

public record AbilityIntent(ActorId actorId, AbilityId abilityId) {
    public AbilityIntent {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(abilityId);
    }
}