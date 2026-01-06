package com.pgalaxyp.fragmento.rpg.core.state.combo;

import java.util.HashMap;
import java.util.Map;

public final class ComboProgressState {

    private final Map<Long, Integer> indexByActor = new HashMap<>();
    private final Map<Long, Long> lastStepAt = new HashMap<>();

    public int index(long actorId) {
        return indexByActor.getOrDefault(actorId, 0);
    }

    public void advance(long actorId, long nowNanos) {
        indexByActor.put(actorId, index(actorId) + 1);
        lastStepAt.put(actorId, nowNanos);
    }

    public void reset(long actorId) {
        indexByActor.remove(actorId);
        lastStepAt.remove(actorId);
    }

    public boolean timedOut(long actorId, long nowNanos, long timeoutNanos) {
        var last = lastStepAt.get(actorId);
        if (last == null) return false;
        return nowNanos - last >= timeoutNanos;
    }
}