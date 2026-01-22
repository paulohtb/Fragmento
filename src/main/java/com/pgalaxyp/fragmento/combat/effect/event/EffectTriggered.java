package com.pgalaxyp.fragmento.combat.effect.event;

import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.effect.api.EffectId;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.Objects;

public record EffectTriggered(EffectId effectId, ActorId source, ActorId target) implements DomainEvent {
    public EffectTriggered {
        Objects.requireNonNull(effectId);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
    }
}
