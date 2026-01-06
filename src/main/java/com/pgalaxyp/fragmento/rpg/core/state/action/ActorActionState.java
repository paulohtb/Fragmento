package com.pgalaxyp.fragmento.rpg.core.state.action;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import java.util.Map;

public record ActorActionState(
        Map<Long, ActionId> active,
        Map<Long, Long> startedAt
) {
    public boolean isIdle(long actorId) {
        return !active.containsKey(actorId);
    }

    public ActorActionState start(long actorId, ActionId id, long nowNanos) {
        var a = new java.util.HashMap<>(active);
        var t = new java.util.HashMap<>(startedAt);
        a.put(actorId, id);
        t.put(actorId, nowNanos);
        return new ActorActionState(a, t);
    }

    public ActorActionState clear(long actorId) {
        var a = new java.util.HashMap<>(active);
        var t = new java.util.HashMap<>(startedAt);
        a.remove(actorId);
        t.remove(actorId);
        return new ActorActionState(a, t);
    }

    public static ActorActionState empty() {
        return new ActorActionState(Map.of(), Map.of());
    }
}