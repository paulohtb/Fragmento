package com.pgalaxyp.fragmento.combat.effectModule.event;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.random.FrameEvent;
import com.pgalaxyp.fragmento.combat.effectModule.api.EffectId;

import java.util.Objects;

public record EffectTriggered(EffectId effectId, ActorId source, ActorId target) implements FrameEvent {
    public EffectTriggered {
        Objects.requireNonNull(effectId);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
    }
}
