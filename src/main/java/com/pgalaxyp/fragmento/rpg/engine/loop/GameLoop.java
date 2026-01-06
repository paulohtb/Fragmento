package com.pgalaxyp.fragmento.rpg.engine.loop;

import java.util.Objects;
import java.util.function.LongSupplier;

public final class GameLoop {
    private final LongSupplier nowNanos;
    private final UpdateScheduler scheduler;
    private final TickBus bus;

    private long lastNow;
    private long tickIndex;

    public GameLoop(LongSupplier nowNanos, UpdateScheduler scheduler, TickBus bus) {
        this.nowNanos = Objects.requireNonNull(nowNanos);
        this.scheduler = Objects.requireNonNull(scheduler);
        this.bus = Objects.requireNonNull(bus);
        this.lastNow = nowNanos.getAsLong();
        this.tickIndex = 0L;
    }

    public void tick() {
        var now = nowNanos.getAsLong();
        var delta = Math.max(0L, now - lastNow);
        lastNow = now;

        var tick = new GameTick(now, delta, tickIndex++);
        scheduler.updateAll(tick, bus);
    }

    public TickBus bus() {
        return bus;
    }
}