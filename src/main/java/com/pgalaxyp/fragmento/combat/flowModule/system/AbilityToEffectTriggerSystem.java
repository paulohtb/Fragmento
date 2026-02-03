package com.pgalaxyp.fragmento.combat.flowModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.targetingModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityId;
import com.pgalaxyp.fragmento.combat.effectModule.event.EffectTriggered;
import com.pgalaxyp.fragmento.combat.abilityModule.event.AbilityStarted;
import com.pgalaxyp.fragmento.combat.contentModule.api.AbilityTriggerSpec;
import java.util.*;

public record AbilityToEffectTriggerSystem(Map<AbilityId, AbilityTriggerSpec> bindings, TargetingService targeting) implements FrameSystem {
    public AbilityToEffectTriggerSystem {
        bindings = Map.copyOf(Objects.requireNonNull(bindings));
        Objects.requireNonNull(targeting);
    }

    @Override public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        for (var started : bus.events(AbilityStarted.class)) {
            var snap = started.snapshot();
            var spec = bindings.get(snap.abilityId());
            if (spec == null) continue;
            var actorTarget = targeting.resolve(new TargetingRequest(snap.actorId(), spec.targeting())).actorTargetIdOrNull();
            if (actorTarget != null) bus.publish(new EffectTriggered(spec.effectId(), snap.actorId(), actorTarget));
        }
    }
}