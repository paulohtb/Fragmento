package com.pgalaxyp.fragmento.rpg.engine.store;

import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;

import java.util.HashMap;
import java.util.Map;

public final class CombatStateStore {

    private final Map<Long, ActorState> actors = new HashMap<>();

    public ActorState getOrInitial(long actorId) {
        return actors.getOrDefault(actorId, ActorState.initial(actorId));
    }

    public void putAll(Map<Long, ActorState> updated) {
        actors.putAll(updated);
    }

    public Map<Long, ActorState> snapshot() {
        return Map.copyOf(actors);
    }
}