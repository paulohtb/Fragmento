package com.pgalaxyp.fragmento.rpg.core.state.action;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ActorActionState {

    private final Map<Long, ActionId> active = new HashMap<>();
    private final Map<Long, Long> startedAt = new HashMap<>();

    public boolean isIdle(long actorId) {
        return !active.containsKey(actorId);
    }

    public Optional<ActionId> active(long actorId) {
        return Optional.ofNullable(active.get(actorId));
    }

    public void start(long actorId, ActionId id, long nowNanos) {
        active.put(actorId, id);
        startedAt.put(actorId, nowNanos);
    }

    public boolean isActiveFor(long actorId, ActionId id) {
        return id.equals(active.get(actorId));
    }

    public void clear(long actorId) {
        active.remove(actorId);
        startedAt.remove(actorId);
    }
}