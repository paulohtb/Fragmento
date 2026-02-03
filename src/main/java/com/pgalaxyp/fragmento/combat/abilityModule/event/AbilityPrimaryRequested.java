package com.pgalaxyp.fragmento.combat.abilityModule.event;

import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import java.util.Objects;

public record AbilityPrimaryRequested(ActorId actorId, WeaponId weaponId) implements FrameEvent {
    public AbilityPrimaryRequested {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(weaponId);
    }
}