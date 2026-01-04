package com.pgalaxyp.fragmento.rpg.time;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;

public final class TickClock implements RpgClock {

    private final TickSource source;

    public TickClock(TickSource source) {
        this.source = source;
    }

    @Override
    public Time now() {
        long t = source != null ? source.gameTick() : 0L;
        return Time.ofTicks(t);
    }
}