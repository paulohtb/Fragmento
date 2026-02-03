package com.pgalaxyp.fragmento.combat.effectModule.event;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.effectModule.api.EffectId;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import java.util.Objects;

public record EffectResolved(EffectId effectId, ActorId source, ActorId target) implements FrameEvent {
    public EffectResolved {
        Objects.requireNonNull(effectId);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
    }
}