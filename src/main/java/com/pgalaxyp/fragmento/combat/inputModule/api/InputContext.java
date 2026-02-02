package com.pgalaxyp.fragmento.combat.inputModule.api;

import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import java.util.Optional;

public record InputContext(ActorId actorId, long snapshotFrameId, WeaponId weaponInHandId) {
    public InputContext { if (snapshotFrameId < 0) throw new IllegalArgumentException(); }

    public Optional<ActorId> actorIdOpt() { return Optional.ofNullable(actorId); }
    public boolean hasWeaponInHand() { return weaponInHandId != null; }
}