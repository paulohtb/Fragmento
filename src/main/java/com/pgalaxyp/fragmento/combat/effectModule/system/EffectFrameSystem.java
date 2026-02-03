package com.pgalaxyp.fragmento.combat.effectModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.effectModule.api.*;
import com.pgalaxyp.fragmento.combat.effectModule.event.EffectTriggered;
import java.util.Objects;

public record EffectFrameSystem(EffectService effects) implements FrameSystem {
    public EffectFrameSystem {
        Objects.requireNonNull(effects);
    }

    @Override public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        for (var t : bus.events(EffectTriggered.class)) effects.applyResolved(frame, t.effectId(), t.source(), t.target()).events().forEach(bus::publish);
    }
}