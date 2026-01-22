package com.pgalaxyp.fragmento.combat.core.state;

import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.actor.ActorState;
import com.pgalaxyp.fragmento.combat.core.time.*;
import java.util.*;

public record GameState(FrameContext frame, NavigableMap<ActorId, ActorState> actors) {
    public GameState {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(actors);
        for (var e : actors.entrySet()) if (e.getKey() == null || e.getValue() == null) throw new IllegalArgumentException();
        actors = Collections.unmodifiableNavigableMap(new TreeMap<>(actors));
    }

    public Optional<ActorState> findActor(ActorId actorId) {
        if (actorId == null) throw new IllegalArgumentException();
        return Optional.ofNullable(actors.get(actorId));
    }

    public ActorState actor(ActorId actorId) {
        ActorState state = actors.get(actorId);
        if (state == null) throw new IllegalArgumentException();
        return state;
    }

    public static GameState empty(FrameContext frame) { return new GameState(frame, new TreeMap<>()); }
}