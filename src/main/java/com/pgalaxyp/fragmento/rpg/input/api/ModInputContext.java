package com.pgalaxyp.fragmento.rpg.input.api;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import com.pgalaxyp.fragmento.rpg.input.bridge.SnapshotView;
import java.util.Optional;

public record ModInputContext(
        ActorId actorId,
        SnapshotView snapshot,
        Optional<WeaponId> weaponInHandId
) {
    public ModInputContext {
        if (actorId == null || snapshot == null || weaponInHandId == null) {
            throw new IllegalArgumentException();
        }
        if (weaponInHandId.isPresent() && weaponInHandId.get() == null) {
            throw new IllegalArgumentException();
        }
        weaponInHandId = weaponInHandId.isPresent() ? Optional.of(weaponInHandId.get()) : Optional.empty();
    }

    public boolean hasWeaponInHand() {
        return weaponInHandId.isPresent();
    }

    public Optional<WeaponId> activeWeaponId() {
        return weaponInHandId;
    }
}