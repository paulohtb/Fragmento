package com.pgalaxyp.fragmento.combat.events.ability;

import com.pgalaxyp.fragmento.combat.ability.api.AbilitySnapshot;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.Objects;

public record AbilityEnded(ActorId actorId, AbilitySnapshot snapshot) implements DomainEvent {
    public AbilityEnded {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(snapshot);
    }
}
