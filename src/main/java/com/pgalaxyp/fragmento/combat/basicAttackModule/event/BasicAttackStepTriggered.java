package com.pgalaxyp.fragmento.combat.basicAttackModule.event;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.Objects;

public record BasicAttackStepTriggered(ActorId actorId, WeaponId weaponId, int stepIndex) implements FrameEvent {
    public BasicAttackStepTriggered {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(weaponId);
        if (stepIndex < 0) throw new IllegalArgumentException();
    }
}
