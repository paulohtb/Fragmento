package com.pgalaxyp.fragmento.combat.damageModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.damageModule.port.DamagePort;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageRequested;
import java.util.Objects;

public record DamageFrameSystem(DamagePort damage) implements FrameSystem {
    public DamageFrameSystem { Objects.requireNonNull(damage); }

    @Override public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        for (var req : bus.events(DamageRequested.class)) damage.resolve(req).events().forEach(bus::publish);
    }
}