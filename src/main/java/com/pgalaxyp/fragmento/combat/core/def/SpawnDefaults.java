package com.pgalaxyp.fragmento.combat.core.def;

import com.pgalaxyp.fragmento.combat.core.ids.*;

public record SpawnDefaults(ClassId classId, WeaponId startingWeaponId, int healthHearts, int maxHealthHearts) {
    public SpawnDefaults {
        if (classId == null || startingWeaponId == null) throw new IllegalArgumentException();
        if (healthHearts < 0 || maxHealthHearts <= 0 || healthHearts > maxHealthHearts) throw new IllegalArgumentException();
    }

    public static SpawnDefaults of(ClassId classId, WeaponId startingWeaponId, int healthHearts, int maxHealthHearts) {
        return new SpawnDefaults(classId, startingWeaponId, healthHearts, maxHealthHearts);
    }
}
