package com.pgalaxyp.fragmento.combat.damageModule.system;

import com.pgalaxyp.fragmento.combat.random.*;
import com.pgalaxyp.fragmento.combat.damageModule.port.DamagePort;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageRequested;
import java.util.Objects;

public final class DamageExecution implements FrameSystem {
    private final DamagePort damage;

    public DamageExecution(DamagePort damage) {
        this.damage = Objects.requireNonNull(damage);
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);

        for (DamageRequested e : bus.events(DamageRequested.class)) {
            damage.resolve(e, frame, state).events().forEach(bus::publish);
        }
    }
}