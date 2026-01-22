package com.pgalaxyp.fragmento.combat.ability.api;

import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.effect.api.EffectId;
import com.pgalaxyp.fragmento.combat.targeting.api.TargetResult;
import java.util.Objects;

public sealed interface AbilityEvent permits AbilityEvent.Started, AbilityEvent.Ended, AbilityEvent.Rejected {
    record Started(AbilitySnapshot snapshot, EffectId startEffect, TargetResult targeting, ActorId source, ActorId target) implements AbilityEvent {
        public Started {
            Objects.requireNonNull(snapshot);
            Objects.requireNonNull(startEffect);
            Objects.requireNonNull(targeting);
            Objects.requireNonNull(source);
            Objects.requireNonNull(target);
        }
    }

    record Ended(AbilitySnapshot snapshot) implements AbilityEvent {
        public Ended { Objects.requireNonNull(snapshot); }
    }

    record Rejected(ActorId actorId, AbilityId abilityId, AbilityRejectReason reason) implements AbilityEvent {
        public Rejected {
            Objects.requireNonNull(actorId);
            Objects.requireNonNull(abilityId);
            Objects.requireNonNull(reason);
        }
    }
}