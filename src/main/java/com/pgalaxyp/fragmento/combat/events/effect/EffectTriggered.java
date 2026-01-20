package com.pgalaxyp.fragmento.combat.events.effect;

import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.ids.EffectId;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.Objects;

public record EffectTriggered(EffectId effectId, ActorId source, ActorId target) implements DomainEvent {
    public EffectTriggered {
        Objects.requireNonNull(effectId);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
    }
}
