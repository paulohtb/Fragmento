package com.pgalaxyp.fragmento.system.temporal;

public final class BehaviorPhaseMachine<C, P extends Enum<P>> {

    public interface Enter<C, P extends Enum<P>> {
        void enter(C ctx, P phase, int duration);
    }

    public interface Tick<C, P extends Enum<P>> {
        void tick(C ctx, P phase, int time, int duration);
    }

    private P phase;
    private int time;
    private int duration;

    private final Enter<C, P> enter;
    private final Tick<C, P> tick;

    public BehaviorPhaseMachine(Enter<C, P> enter, Tick<C, P> tick) {
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

    public void start(C ctx, P next, int nextDuration) {
        phase = next;
        time = 0;
        duration = Math.max(1, nextDuration);
        enter.enter(ctx, next, duration);
    }

    public void step(C ctx) {
        time++;
        tick.tick(ctx, phase, time, duration);
    }
}