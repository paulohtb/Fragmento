package com.pgalaxyp.fragmento.rpg.input.system;

import java.util.concurrent.atomic.AtomicLong;

public final class LocalClientFrameClock {

    private final AtomicLong frame = new AtomicLong(0L);

    public long now() {
        return frame.get();
    }

    public void tick() {
        frame.incrementAndGet();
    }
}