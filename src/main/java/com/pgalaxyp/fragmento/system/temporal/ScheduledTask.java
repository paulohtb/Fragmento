package com.pgalaxyp.fragmento.system.temporal;

public abstract class ScheduledTask {

    private final long intervalTicks;
    private long nextRunTick;
    private boolean finished;

    protected ScheduledTask(long startTick, long intervalTicks) {
        this.intervalTicks = Math.max(1, intervalTicks);
        this.nextRunTick = startTick;
    }

    public final boolean shouldRun(long now) {
        return !finished && now >= nextRunTick;
    }

    public final void run(long now) {
        execute(now);
        nextRunTick = now + intervalTicks;
    }

    protected abstract void execute(long now);

    protected final void finish() {
        finished = true;
    }

    public final boolean isFinished() {
        return finished;
    }
}