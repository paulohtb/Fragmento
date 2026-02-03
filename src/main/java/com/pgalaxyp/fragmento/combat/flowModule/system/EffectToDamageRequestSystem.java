package com.pgalaxyp.fragmento.combat.flowModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.effectModule.api.EffectId;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageSpec;
import com.pgalaxyp.fragmento.combat.effectModule.event.EffectResolved;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageRequested;
import java.util.*;

public record EffectToDamageRequestSystem(Map<EffectId, DamageSpec> effects) implements FrameSystem {
    public EffectToDamageRequestSystem {
        effects = Map.copyOf(Objects.requireNonNull(effects));
    }

    @Override public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        for (var e : bus.events(EffectResolved.class)) {
            DamageSpec spec = effects.get(e.effectId());
            if (spec != null) bus.publish(new DamageRequested(e.source(), e.target(), spec));
        }
    }
}