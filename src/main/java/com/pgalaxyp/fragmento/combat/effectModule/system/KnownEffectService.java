package com.pgalaxyp.fragmento.combat.effectModule.system;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.effectModule.api.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import com.pgalaxyp.fragmento.combat.effectModule.event.EffectResolved;
import java.util.*;

public record KnownEffectService(Set<EffectId> knownEffects) implements EffectService {
    public KnownEffectService {
        knownEffects = Set.copyOf(Objects.requireNonNull(knownEffects));
    }

    @Override public EffectOutcome applyResolved(FrameContext frame, EffectId effectId, ActorId source, ActorId target) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(effectId);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        return knownEffects.contains(effectId) ? new EffectOutcome(List.of(new EffectResolved(effectId, source, target))) : EffectOutcome.empty();
    }
}