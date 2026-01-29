package com.pgalaxyp.fragmento.combat.effectModule.event;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.effectModule.api.EffectId;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageSpec;
import java.util.Objects;

public record EffectApplied(EffectId effectId, ActorId source, ActorId target, DamageSpec damage) implements FrameEvent {
    public EffectApplied {
        Objects.requireNonNull(effectId);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        Objects.requireNonNull(damage);
    }
}