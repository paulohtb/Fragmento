package com.pgalaxyp.fragmento.rpg.core.boot;

import com.pgalaxyp.fragmento.rpg.core.loop.GameLoop;
import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.core.loop.UpdateScheduler;

import java.util.Objects;
import java.util.function.LongSupplier;

public final class GameBootstrap {

    private final LongSupplier nowNanos;
    private final ModuleWiring wiring;
    private final TickBus bus;

    public GameBootstrap(LongSupplier nowNanos, ModuleWiring wiring, TickBus bus) {
        this.nowNanos = Objects.requireNonNull(nowNanos);
        this.wiring = Objects.requireNonNull(wiring);
        this.bus = Objects.requireNonNull(bus);
    }

    public GameLoop build() {
        var scheduler = new UpdateScheduler();
        wiring.registerSystems(scheduler, bus);
        return new GameLoop(nowNanos, scheduler, bus);
    }
}