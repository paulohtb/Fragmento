package com.pgalaxyp.fragmento.rpg.domain.timing;

public record Time(long ticks) {

    public static Time ofTicks(long ticks) {
        return new Time(Math.max(0L, ticks));
    }

    public Time plus(Duration d) {
        if (d == null) {
            return this;
        }
        return new Time(ticks + Math.max(0L, d.ticks()));
    }

    public boolean isAfterOrEqual(Time other) {
        if (other == null) {
            return false;
        }
        return ticks >= other.ticks();
    }

    public boolean isBefore(Time other) {
        if (other == null) {
            return false;
        }
        return ticks < other.ticks();
    }
}