package com.pgalaxyp.fragmento.combat.damageModule.event;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageSpec;
import java.util.Objects;

public record DamageApplied(ActorId sourceActorId, ActorId targetActorId, DamageSpec spec, int damageHearts) implements FrameEvent {
    public DamageApplied {
        Objects.requireNonNull(sourceActorId);
        Objects.requireNonNull(targetActorId);
        Objects.requireNonNull(spec);
        if (damageHearts <= 0) throw new IllegalArgumentException();
    }
}