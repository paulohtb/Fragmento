package com.pgalaxyp.fragmento.combat.actionModule.event;

import com.pgalaxyp.fragmento.combat.actionModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.Objects;

public record ActionRejected(ActorId actorId, ActionSlot slot, WeaponId weaponId, ActionRejectReason reason) {
    public ActionRejected {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(slot);
        Objects.requireNonNull(weaponId);
        Objects.requireNonNull(reason);
    }
}
