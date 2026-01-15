package com.pgalaxyp.fragmento.combat.input.system;

import com.pgalaxyp.fragmento.combat.input.api.*;
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