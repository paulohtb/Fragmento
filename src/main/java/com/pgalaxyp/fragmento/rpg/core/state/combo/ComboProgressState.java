package com.pgalaxyp.fragmento.rpg.core.state.combo;

import java.util.Map;

public record ComboProgressState(
        Map<Long, Integer> indexByActor,
        Map<Long, Long> lastStepAt
) {
    public int index(long actorId) {
        return indexByActor.getOrDefault(actorId, 0);
    }

    public boolean timedOut(long actorId, long nowNanos, long timeoutNanos) {
        var last = lastStepAt.get(actorId);
        if (last == null) return false;
        return nowNanos - last >= timeoutNanos;
    }

    public ComboProgressState advance(long actorId, long nowNanos) {
        var i = new java.util.HashMap<>(indexByActor);
        var t = new java.util.HashMap<>(lastStepAt);
        i.put(actorId, index(actorId) + 1);
        t.put(actorId, nowNanos);
        return new ComboProgressState(i, t);
    }

    public ComboProgressState reset(long actorId) {
        var i = new java.util.HashMap<>(indexByActor);
        var t = new java.util.HashMap<>(lastStepAt);
        i.remove(actorId);
        t.remove(actorId);
        return new ComboProgressState(i, t);
    }

    public static ComboProgressState empty() {
        return new ComboProgressState(Map.of(), Map.of());
    }
}