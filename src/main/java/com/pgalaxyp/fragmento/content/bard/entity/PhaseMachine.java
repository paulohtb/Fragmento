package com.pgalaxyp.fragmento.content.bard.entity;

public final class PhaseMachine<P extends Enum<P>> {

    public interface Enter<P extends Enum<P>> {
        void enter(P phase, int duration);
    }

    public interface Tick<P extends Enum<P>> {
        void tick(P phase, int time, int duration);
    }

    private P phase;
    private int time;
    private int duration;

    private final Enter<P> enter;
    private final Tick<P> tick;

    public PhaseMachine(Enter<P> enter, Tick<P> tick) {
        this.enter = enter;
        this.tick = tick;
    }

    public P phase() {
        return phase;
    }

    public int time() {
        return time;
    }

    public int duration() {
        return duration;
    }

    public void start(P next, int nextDuration) {
        phase = next;
        time = 0;
        duration = Math.max(1, nextDuration);
        enter.enter(next, duration);
    }

    public void step() {
        time++;
        tick.tick(phase, time, duration);
    }
}
