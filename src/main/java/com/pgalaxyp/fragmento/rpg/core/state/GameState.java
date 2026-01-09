package com.pgalaxyp.fragmento.rpg.core.state;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import java.util.Map;
import java.util.Optional;

public record GameState(
        FrameContext frame,
        Map<ActorId, ActorState> actors
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
        actors = Map.copyOf(actors);
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