package com.pgalaxyp.fragmento.combat.actorModule.api;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class DefaultActorService implements ActorService {
    private final Set<ActorId> tracked = ConcurrentHashMap.newKeySet();

    @Override
    public boolean track(ActorId actorId) {
        return tracked.add(Objects.requireNonNull(actorId));
    }

    @Override
    public boolean isTracked(ActorId actorId) {
        return tracked.contains(Objects.requireNonNull(actorId));
    }
}