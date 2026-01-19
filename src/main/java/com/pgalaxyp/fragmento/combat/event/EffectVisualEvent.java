package com.pgalaxyp.fragmento.combat.event;

import com.pgalaxyp.fragmento.combat.core.ids.*;

public record EffectVisualEvent(long frameId, int localIndex, EffectId effectId, ActorId sourceActorId, ActorId targetActorId, int lifetimeFrames) implements DomainEvent {
    public EffectVisualEvent {
        if (frameId < 0 || localIndex < 0) throw new IllegalArgumentException();
        if (effectId == null || sourceActorId == null || targetActorId == null) throw new IllegalArgumentException();
        if (lifetimeFrames <= 0) throw new IllegalArgumentException();
    }
}