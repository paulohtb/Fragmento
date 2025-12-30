package com.pgalaxyp.fragmento.combat.old.system.temporal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class TemporalScheduler {

    private final List<ScheduledTask> tasks = new ArrayList<>(64);

    public void schedule(ScheduledTask task) {
        if (task == null) return;
        tasks.add(task);
    }

    public void tick(long gameTime, long budgetNanos) {
        long start = System.nanoTime();
        Iterator<ScheduledTask> it = tasks.iterator();

        while (it.hasNext()) {
            ScheduledTask task = it.next();
            if (!task.shouldRun(gameTime)) continue;

            task.run(gameTime);

            if (task.isFinished()) {
                it.remove();
            }

            if (System.nanoTime() - start >= budgetNanos) {
                break;
            }
        }
    }
}