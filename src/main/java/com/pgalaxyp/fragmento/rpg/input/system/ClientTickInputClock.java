package com.pgalaxyp.fragmento.rpg.input.system;

import com.pgalaxyp.fragmento.rpg.input.api.ModInputContext;

public final class ClientTickInputClock implements InputClock {

    private final LocalClientFrameClock clock;

    public ClientTickInputClock(LocalClientFrameClock clock) {
        if (clock == null) {
            throw new IllegalArgumentException();
        }
        this.clock = clock;
    }

    @Override
    public long frameId(ModInputContext context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        return clock.now();
    }
}