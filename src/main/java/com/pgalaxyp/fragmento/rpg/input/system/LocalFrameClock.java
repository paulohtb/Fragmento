package com.pgalaxyp.fragmento.rpg.input.system;

import com.pgalaxyp.fragmento.rpg.input.api.*;
import java.util.concurrent.atomic.*;

public final class LocalFrameClock implements FrameClock {

    private final AtomicLong frame = new AtomicLong(0L);

    @Override
    public long frameId(InputContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        return frame.get();
    }

    @Override
    public void tick() {
        frame.incrementAndGet();
    }
}