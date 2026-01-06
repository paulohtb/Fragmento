package com.pgalaxyp.fragmento.rpg.engine.store;

import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import java.util.HashMap;
import java.util.Map;

public final class CombatStateStore {

    private final Map<Long, ActorState> actors = new HashMap<>();

    public ActorState get(long actorId) {
        return actors.get(actorId);
    }

    public void put(long actorId, ActorState state) {
        actors.put(actorId, state);
    }

    Map<Long, ActorState> copyActors() {
        return Map.copyOf(actors);
    }
}