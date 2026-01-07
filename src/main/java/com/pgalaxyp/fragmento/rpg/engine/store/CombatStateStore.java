package com.pgalaxyp.fragmento.rpg.engine.store;

import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;

import java.util.HashMap;
import java.util.Map;

public final class CombatStateStore {

    private final Map<Long, ActorState> actors = new HashMap<>();

    public ActorState getOrEmpty(long actorId) {
        return actors.getOrDefault(actorId, ActorState.empty(actorId));
    }

    public void put(ActorState state) {
        if (state == null) return;
        actors.put(state.actorId(), state);
    }

    public Map<Long, ActorState> snapshot() {
        return Map.copyOf(actors);
    }
}