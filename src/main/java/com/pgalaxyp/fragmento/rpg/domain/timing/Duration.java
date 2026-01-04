package com.pgalaxyp.fragmento.rpg.domain.timing;

public record Duration(long ticks) {

    public static Duration ofTicks(long ticks) {
        return new Duration(Math.max(0, ticks));
    }

    public static Duration ofSeconds(double seconds, int ticksPerSecond) {
        if (seconds <= 0.0 || ticksPerSecond <= 0) {
            return new Duration(0L);
        }
        long t = Math.round(seconds * (double) ticksPerSecond);
        return new Duration(Math.max(0L, t));
    }

    public boolean isZero() {
        return ticks <= 0;
    }
}