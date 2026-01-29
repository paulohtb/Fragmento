package com.pgalaxyp.fragmento.combat.effectModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.effectModule.api.*;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import com.pgalaxyp.fragmento.combat.effectModule.event.EffectTriggered;
import java.util.Objects;

public record EffectSystem(EffectService effects) implements FrameSystem {
    public EffectSystem { Objects.requireNonNull(effects); }

    @Override public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        var gs = (GameState) state;
        for (var t : bus.events(EffectTriggered.class)) effects.applyResolved(frame, gs, t.effectId(), t.source(), t.target()).events().forEach(bus::publish);
    }
}