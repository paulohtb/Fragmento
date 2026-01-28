package com.pgalaxyp.fragmento.combat.damageModule.event;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import java.util.Objects;

public record DamageApplied(ActorId targetActorId, int hearts) implements FrameEvent {
    public DamageApplied {
        Objects.requireNonNull(targetActorId);
        if (hearts <= 0) throw new IllegalArgumentException();
    }
}