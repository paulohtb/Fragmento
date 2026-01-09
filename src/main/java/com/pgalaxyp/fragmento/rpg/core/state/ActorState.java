package com.pgalaxyp.fragmento.rpg.core.state;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ClassId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import java.util.Optional;

public record ActorState(
        ClassId classId,
        Optional<WeaponId> equippedWeaponId,
        Optional<ComboState> combo,
        int healthHearts,
        int maxHealthHearts
) {
    public ActorState {
        if (classId == null || equippedWeaponId == null || combo == null) {
            throw new IllegalArgumentException();
        }
        if (healthHearts < 0 || maxHealthHearts <= 0 || healthHearts > maxHealthHearts) {
            throw new IllegalArgumentException();
        }
        if (combo.isPresent()) {
            if (equippedWeaponId.isEmpty()) {
                throw new IllegalArgumentException();
            }
            if (!equippedWeaponId.get().equals(combo.get().weaponId())) {
                throw new IllegalArgumentException();
            }
        }
    }

    public static ActorState idleWithHealth(ClassId classId, int healthHearts, int maxHealthHearts) {
        if (classId == null) {
            throw new IllegalArgumentException();
        }
        return new ActorState(classId, Optional.empty(), Optional.empty(), healthHearts, maxHealthHearts);
    }

    public static ActorState withEquippedWeapon(ClassId classId, WeaponId weaponId, int healthHearts, int maxHealthHearts) {
        if (classId == null || weaponId == null) {
            throw new IllegalArgumentException();
        }
        return new ActorState(classId, Optional.of(weaponId), Optional.empty(), healthHearts, maxHealthHearts);
    }
}