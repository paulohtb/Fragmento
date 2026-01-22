package com.pgalaxyp.fragmento.combat.ability.event;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.Objects;

public record AbilityRequested(ActorId actorId, AbilityId abilityId, WeaponId weaponId) implements DomainEvent {
    public AbilityRequested {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(abilityId);
        Objects.requireNonNull(weaponId);
    }
}