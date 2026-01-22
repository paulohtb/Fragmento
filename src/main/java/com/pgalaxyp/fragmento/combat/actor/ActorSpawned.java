package com.pgalaxyp.fragmento.combat.actor;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;

public record ActorSpawned(ActorId actorId, ClassId classId, WeaponId equippedWeaponId, int healthHearts, int maxHealthHearts) implements DomainEvent {
    public ActorSpawned {
        if (actorId == null || classId == null || equippedWeaponId == null) { throw new IllegalArgumentException(); }
        if (healthHearts < 0 || maxHealthHearts <= 0 || healthHearts > maxHealthHearts) { throw new IllegalArgumentException(); }
    }
}