package com.pgalaxyp.fragmento.rpg.domain.timing;

public record Time(long ticks) {

    public static final Time ZERO = new Time(0L);

    public static Time ofTicks(long ticks) {
        return new Time(ticks);
    }

    public Time plus(Time other) {
        if (other == null) return this;
        return new Time(this.ticks + other.ticks);
    }

    public Time plus(Duration duration) {
        if (duration == null) return this;
        return new Time(this.ticks + duration.ticks());
    }

    public boolean isAfter(Time other) {
        if (other == null) return false;
        return ticks > other.ticks;
    }

    public boolean isBefore(Time other) {
        if (other == null) return false;
        return ticks < other.ticks;
    }

    public boolean isAfterOrEqual(Time other) {
        if (other == null) return true;
        return ticks >= other.ticks;
    }
}