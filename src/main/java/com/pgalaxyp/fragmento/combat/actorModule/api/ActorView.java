package com.pgalaxyp.fragmento.combat.actorModule.api;

import java.util.*;

public record ActorView(NavigableMap<ActorId, ActorState> actors) {
    private static final ActorView EMPTY = new ActorView(new TreeMap<>());

    public ActorView {
        Objects.requireNonNull(actors);
        for (var e : actors.entrySet()) if (e.getKey() == null || e.getValue() == null) throw new IllegalArgumentException();
        actors = Collections.unmodifiableNavigableMap(new TreeMap<>(actors));
    }

    public static ActorView empty() { return EMPTY; }
    public NavigableSet<ActorId> ids() { return actors.navigableKeySet(); }

    public Optional<ActorState> findActor(ActorId actorId) {
        if (actorId == null) throw new IllegalArgumentException();
        return Optional.ofNullable(actors.get(actorId));
    }

    public ActorState actor(ActorId actorId) {
        if (actorId == null) throw new IllegalArgumentException();
        ActorState s = actors.get(actorId);
        if (s == null) throw new IllegalArgumentException();
        return s;
    }
}