package com.pgalaxyp.fragmento.rpg.gameplay.time;

import com.pgalaxyp.fragmento.rpg.core.loop.GameTick;

public record GameTime(long nowNanos, long deltaNanos) {
    public static GameTime fromTick(GameTick tick) {
        return new GameTime(tick.nowNanos(), tick.deltaNanos());
    }

    public double deltaSeconds() {
        return deltaNanos / 1_000_000_000.0;
    }
}