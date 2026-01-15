package com.pgalaxyp.fragmento.combat.core.state;

import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

public record GameState(
        FrameContext frame,
        NavigableMap<ActorId, ActorState> actors
) {
    public GameState {
        if (frame == null || actors == null) {
            throw new IllegalArgumentException();
        }
        for (var e : actors.entrySet()) {
            if (e.getKey() == null || e.getValue() == null) {
                throw new IllegalArgumentException();
            }
        }
        actors = Collections.unmodifiableNavigableMap(new TreeMap<>(actors));
    }

    public Optional<ActorState> findActor(ActorId actorId) {
        if (actorId == null) {
            throw new IllegalArgumentException();
        }
        return Optional.ofNullable(actors.get(actorId));
    }

    public ActorState actor(ActorId actorId) {
        ActorState state = actors.get(actorId);
        if (state == null) {
            throw new IllegalArgumentException();
        }
        return state;
    }
}