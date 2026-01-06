package com.pgalaxyp.fragmento.rpg.engine.loop;

public record GameTick(long nowNanos, long deltaNanos, long tickIndex) {
    public double deltaSeconds() {
        return deltaNanos / 1_000_000_000.0;
    }
}