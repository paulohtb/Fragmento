package com.pgalaxyp.fragmento.combat.core.state;

import com.pgalaxyp.fragmento.combat.core.ids.ClassId;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;
import java.util.Optional;

public record ActorState(
        ClassId classId,
        Optional<WeaponId> equippedWeaponId,
        int healthHearts,
        int maxHealthHearts
) {
    public ActorState {
        if (classId == null || equippedWeaponId == null) {
            throw new IllegalArgumentException();
        }
        if (healthHearts < 0 || maxHealthHearts <= 0 || healthHearts > maxHealthHearts) {
            throw new IllegalArgumentException();
        }
        if (equippedWeaponId.isPresent() && equippedWeaponId.get() == null) {
            throw new IllegalArgumentException();
        }

        equippedWeaponId = equippedWeaponId.isPresent() ? Optional.of(equippedWeaponId.get()) : Optional.empty();
    }

    public static ActorState idleWithHealth(ClassId classId, int healthHearts, int maxHealthHearts) {
        if (classId == null) {
            throw new IllegalArgumentException();
        }
        return new ActorState(classId, Optional.empty(), healthHearts, maxHealthHearts);
    }

    public static ActorState withEquippedWeapon(ClassId classId, WeaponId weaponId, int healthHearts, int maxHealthHearts) {
        if (classId == null || weaponId == null) {
            throw new IllegalArgumentException();
        }
        return new ActorState(classId, Optional.of(weaponId), healthHearts, maxHealthHearts);
    }
}