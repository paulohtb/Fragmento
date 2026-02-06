package com.pgalaxyp.fragmento.combat.abilityModule.event;

import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import java.util.Objects;

public record AbilityRejected(ActorId actorId, AbilityId abilityId, AbilityRejectReason reason) implements FrameEvent {
    public AbilityRejected {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(abilityId);
        Objects.requireNonNull(reason);
    }
}