package com.pgalaxyp.fragmento.combat.classModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import java.util.*;

public record ActorClassView(Map<ActorId, ClassId> byActor) {
    private static final ActorClassView EMPTY = new ActorClassView(Map.of());

    public ActorClassView {
        byActor = Collections.unmodifiableMap(Objects.requireNonNull(byActor));
    }

    public static ActorClassView empty() { return EMPTY; }
    public Optional<ClassId> classIdOf(ActorId actorId) { return Optional.ofNullable(byActor.get(Objects.requireNonNull(actorId))); }
}