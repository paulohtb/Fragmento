package com.pgalaxyp.fragmento.combat.input.api;

import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import com.pgalaxyp.fragmento.combat.input.bridge.*;
import java.util.*;

public record InputContext(ActorId actorId, InputSnapshotView snapshot, WeaponId weaponInHandId) {

    public InputContext {
        if (snapshot == null) {
            throw new IllegalArgumentException();
        }
    }

    public Optional<ActorId> actorIdOpt() {
        return Optional.ofNullable(actorId);
    }

    public Optional<WeaponId> weaponInHandIdOpt() {
        return Optional.ofNullable(weaponInHandId);
    }

    public boolean hasWeaponInHand() {
        return weaponInHandId != null;
    }

    public Optional<WeaponId> activeWeaponId() {
        return Optional.ofNullable(weaponInHandId);
    }
}