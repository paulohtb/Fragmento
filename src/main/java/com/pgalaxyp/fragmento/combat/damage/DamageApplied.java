package com.pgalaxyp.fragmento.combat.damage;

import com.pgalaxyp.fragmento.combat.actor.api.ActorId;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.Objects;

public record DamageApplied(ActorId targetActorId, int hearts) implements DomainEvent {
    public DamageApplied {
        Objects.requireNonNull(targetActorId);
        if (hearts <= 0) { throw new IllegalArgumentException(); }
    }
}