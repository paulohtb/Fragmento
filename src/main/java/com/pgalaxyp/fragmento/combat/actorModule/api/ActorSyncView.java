package com.pgalaxyp.fragmento.combat.actorModule.api;

import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import java.util.*;

public record ActorSyncView(Map<ActorId, ClassId> classes, Set<ActorId> liveActorIds) {
    public static final ActorSyncView EMPTY = new ActorSyncView(Map.of(), Set.of());

    public ActorSyncView {
        classes = Map.copyOf(Objects.requireNonNull(classes));
        liveActorIds = Set.copyOf(Objects.requireNonNull(liveActorIds));
        classes.forEach((k, v) -> { if (k == null || v == null) throw new IllegalArgumentException(); });
        liveActorIds.forEach(id -> { if (id == null) throw new IllegalArgumentException(); });
    }

    public Optional<ClassId> classIdOf(ActorId actorId) {
        return Optional.ofNullable(classes.get(Objects.requireNonNull(actorId)));
    }
}