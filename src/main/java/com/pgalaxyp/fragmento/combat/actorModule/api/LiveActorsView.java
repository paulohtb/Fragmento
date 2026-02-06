package com.pgalaxyp.fragmento.combat.actorModule.api;

import java.util.*;

public record LiveActorsView(Set<ActorId> ids) {
    private static final LiveActorsView EMPTY = new LiveActorsView(Set.of());

    public LiveActorsView {
        Objects.requireNonNull(ids);
        for (var id : ids) Objects.requireNonNull(id);
        ids = Set.copyOf(ids);
    }

    public static LiveActorsView empty() { return EMPTY; }
    public boolean contains(ActorId actorId) { return ids.contains(Objects.requireNonNull(actorId)); }
}