package com.pgalaxyp.fragmento.combat.flowModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.targetingModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityId;
import com.pgalaxyp.fragmento.combat.damageModule.port.DamagePort;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageRequest;
import com.pgalaxyp.fragmento.combat.abilityModule.event.AbilityStarted;
import com.pgalaxyp.fragmento.combat.contentModule.api.AbilityTriggerSpec;
import java.util.*;

public record AbilityToDamageSystem(Map<AbilityId, AbilityTriggerSpec> triggers, TargetingService targeting, DamagePort damage) implements FrameSystem {
    public AbilityToDamageSystem {
        triggers = Map.copyOf(Objects.requireNonNull(triggers));
        Objects.requireNonNull(targeting);
        Objects.requireNonNull(damage);
    }

    @Override public void tick(FrameContext frame, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(bus);
        for (var started : bus.events(AbilityStarted.class)) {
            var snap = started.snapshot();
            var spec = triggers.get(snap.abilityId());
            if (spec == null) continue;
            var target = targeting.resolve(new TargetingRequest(snap.actorId(), spec.targeting())).actorTargetIdOrNull();
            if (target == null) continue;
            damage.resolve(new DamageRequest(snap.actorId(), target, spec.damage())).events().forEach(bus::publish);
        }
    }
}