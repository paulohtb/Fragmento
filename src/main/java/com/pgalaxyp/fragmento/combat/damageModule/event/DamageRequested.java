package com.pgalaxyp.fragmento.combat.damageModule.event;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageSpec;
import java.util.Objects;

public record DamageRequested(ActorId sourceActorId, ActorId targetActorId, DamageSpec spec) implements FrameEvent {
    public DamageRequested {
        Objects.requireNonNull(sourceActorId);
        Objects.requireNonNull(targetActorId);
        Objects.requireNonNull(spec);
    }
}