package com.pgalaxyp.fragmento.combat.inputModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.inputModule.port.InputSnapshotView;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;

import java.util.*;

public record InputContext(ActorId actorId, InputSnapshotView snapshot, WeaponId weaponInHandId) {
    public InputContext { Objects.requireNonNull(snapshot); }

    public Optional<ActorId> actorIdOpt() { return Optional.ofNullable(actorId); }

    public boolean hasWeaponInHand() { return weaponInHandId != null; }
}