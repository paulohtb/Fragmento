package com.pgalaxyp.fragmento.rpg.input.api;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import com.pgalaxyp.fragmento.rpg.input.bridge.InputSnapshotView;
import java.util.Optional;

public record InputContext(
        Optional<ActorId> actorId,
        InputSnapshotView snapshot,
        Optional<WeaponId> weaponInHandId
) {
    public InputContext {
        if (actorId == null || snapshot == null || weaponInHandId == null) {
            throw new IllegalArgumentException();
        }
        if (actorId.isPresent() && actorId.get() == null) {
            throw new IllegalArgumentException();
        }
        if (weaponInHandId.isPresent() && weaponInHandId.get() == null) {
            throw new IllegalArgumentException();
        }

        actorId = actorId.isPresent() ? Optional.of(actorId.get()) : Optional.empty();
        weaponInHandId = weaponInHandId.isPresent() ? Optional.of(weaponInHandId.get()) : Optional.empty();
    }

    public boolean hasWeaponInHand() {
        return weaponInHandId.isPresent();
    }

    public Optional<WeaponId> activeWeaponId() {
        return weaponInHandId;
    }
}