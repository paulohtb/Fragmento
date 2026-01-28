package com.pgalaxyp.fragmento.combat.damageModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.effectModule.api.*;
import com.pgalaxyp.fragmento.combat.effectModule.event.EffectTriggered;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageRequested;
import java.util.*;

public final class DamageFromEffectExecution implements FrameSystem {
    private final Map<EffectId, EffectDef> effects;

    public DamageFromEffectExecution(Map<EffectId, EffectDef> effects) {
        this.effects = Map.copyOf(Objects.requireNonNull(effects));
    }

    @Override
    public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        for (var e : bus.events(EffectTriggered.class)) {
            var def = effects.get(e.effectId());
            if (def != null) bus.publish(new DamageRequested(e.source(), e.target(), def.damage()));
        }
    }
}