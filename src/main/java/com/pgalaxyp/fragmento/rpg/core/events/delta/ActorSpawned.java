package com.pgalaxyp.fragmento.rpg.core.events.delta;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ClassId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;

public record ActorSpawned(
        ActorId actorId,
        ClassId classId,
        WeaponId equippedWeaponId,
        int healthHearts,
        int maxHealthHearts
) implements StateDelta {
    public ActorSpawned {
        if (actorId == null || classId == null || equippedWeaponId == null) {
            throw new IllegalArgumentException();
        }
        if (healthHearts < 0 || maxHealthHearts <= 0 || healthHearts > maxHealthHearts) {
            throw new IllegalArgumentException();
        }
    }
}