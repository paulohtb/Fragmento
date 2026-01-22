package com.pgalaxyp.fragmento.combat.ability.event;

import com.pgalaxyp.fragmento.combat.ability.api.AbilitySnapshot;
import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.effect.api.EffectId;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import com.pgalaxyp.fragmento.combat.targeting.api.TargetResult;
import java.util.Objects;

public record AbilityStarted(
        ActorId actorId,
        AbilitySnapshot snapshot,
        EffectId startEffect,
        TargetResult targeting,
        ActorId source,
        ActorId target
) implements DomainEvent {
    public AbilityStarted {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(snapshot);
        Objects.requireNonNull(startEffect);
        Objects.requireNonNull(targeting);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
    }
}
