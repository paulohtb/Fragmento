package com.pgalaxyp.fragmento.combat.damageModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import com.pgalaxyp.fragmento.combat.damageModule.port.DamagePort;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageRequested;
import java.util.Objects;

public final class DamageExecution implements FrameSystem {
    private final DamagePort damage;

    public DamageExecution(DamagePort damage) {
        this.damage = Objects.requireNonNull(damage);
    }

    @Override
    public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        var gameState = (GameState) state;
        for (var requested : bus.events(DamageRequested.class)) {
            damage.resolve(requested, frame, gameState).events().forEach(bus::publish);
        }
    }
}