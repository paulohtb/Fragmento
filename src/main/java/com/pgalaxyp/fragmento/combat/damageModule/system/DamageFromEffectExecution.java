package com.pgalaxyp.fragmento.combat.damageModule.system;

import com.pgalaxyp.fragmento.combat.random.*;
import com.pgalaxyp.fragmento.combat.effectModule.api.*;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageRequested;
import com.pgalaxyp.fragmento.combat.effectModule.event.EffectTriggered;
import java.util.*;

public final class DamageFromEffectExecution implements FrameSystem {
    private final Map<EffectId, EffectDef> effects;

    public DamageFromEffectExecution(Map<EffectId, EffectDef> effects) {
        this.effects = Map.copyOf(Objects.requireNonNull(effects));
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);

        for (EffectTriggered e : bus.events(EffectTriggered.class)) {
            EffectDef def = effects.get(e.effectId());
            if (def == null) continue;
            bus.publish(new DamageRequested(e.source(), e.target(), def.damage()));
        }
    }
}