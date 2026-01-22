package com.pgalaxyp.fragmento.combat.input.api;

import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.input.bridge.*;
import java.util.*;

public record InputContext(ActorId actorId, InputSnapshotView snapshot, WeaponId weaponInHandId) {
    public InputContext { Objects.requireNonNull(snapshot); }

    public Optional<ActorId> actorIdOpt() { return Optional.ofNullable(actorId); }

    public boolean hasWeaponInHand() { return weaponInHandId != null; }
}