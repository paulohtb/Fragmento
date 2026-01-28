package com.pgalaxyp.fragmento.combat.abilityModule.event;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.effectModule.api.EffectId;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetResult;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilitySnapshot;
import java.util.Objects;

public record AbilityStarted(AbilitySnapshot snapshot, EffectId startEffect, TargetResult targeting, ActorId source) implements FrameEvent {
    public AbilityStarted {
        Objects.requireNonNull(snapshot);
        Objects.requireNonNull(startEffect);
        Objects.requireNonNull(targeting);
        Objects.requireNonNull(source);
    }
}