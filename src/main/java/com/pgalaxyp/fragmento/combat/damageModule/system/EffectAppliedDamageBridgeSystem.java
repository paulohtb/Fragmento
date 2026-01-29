package com.pgalaxyp.fragmento.combat.damageModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.effectModule.event.EffectApplied;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageRequested;
import java.util.Objects;

public record EffectAppliedDamageBridgeSystem() implements FrameSystem {
    @Override public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        for (var e : bus.events(EffectApplied.class)) bus.publish(new DamageRequested(e.source(), e.target(), e.damage()));
    }
}