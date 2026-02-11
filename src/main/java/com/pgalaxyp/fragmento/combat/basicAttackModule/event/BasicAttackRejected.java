package com.pgalaxyp.fragmento.combat.basicAttackModule.event;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.basicAttackModule.api.BasicAttackRejectReason;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.Objects;

public record BasicAttackRejected(ActorId actorId, WeaponId weaponId, BasicAttackRejectReason reason) implements FrameEvent {
    public BasicAttackRejected {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(weaponId);
        Objects.requireNonNull(reason);
    }
}
