package com.pgalaxyp.fragmento.combat.abilityModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import java.util.Objects;

public record AbilityStartRequest(ActorId actorId, AbilityId abilityId) {
    public AbilityStartRequest {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(abilityId);
    }
}
