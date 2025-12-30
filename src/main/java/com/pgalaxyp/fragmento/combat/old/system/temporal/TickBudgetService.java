package com.pgalaxyp.fragmento.combat.old.system.temporal;

import java.util.EnumMap;
import java.util.Map;

public final class TickBudgetService {

    public enum Phase {
        CHANNEL,
        AREA,
        ENTITY,
        OTHER
    }

    private final Map<Phase, Long> spent = new EnumMap<>(Phase.class);
    private final long maxNanos;

    private long phaseStart;

    public TickBudgetService(long maxNanos) {
        this.maxNanos = maxNanos;
        for (Phase p : Phase.values()) {
            spent.put(p, 0L);
        }
    }

    public boolean tryBegin(Phase phase) {
        if (remaining() <= 0) return false;
        phaseStart = System.nanoTime();
        return true;
    }

    public void end(Phase phase) {
        long delta = System.nanoTime() - phaseStart;
        spent.put(phase, spent.get(phase) + delta);
    }

    public long remaining() {
        long used = 0;
        for (long v : spent.values()) {
            used += v;
        }
        return maxNanos - used;
    }

    public void reset() {
        for (Phase p : Phase.values()) {
            spent.put(p, 0L);
        }
    }
}