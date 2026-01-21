package com.pgalaxyp.fragmento.combat.events.ability;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import com.pgalaxyp.fragmento.combat.ability.api.AbilityRejectReason;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.Objects;

public record AbilityRejected(ActorId actorId, AbilityId abilityId, AbilityRejectReason reason) implements DomainEvent {
    public AbilityRejected {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(abilityId);
        Objects.requireNonNull(reason);
    }
}