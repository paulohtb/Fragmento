package com.pgalaxyp.fragmento.combat.delta;

import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.ids.ClassId;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;

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