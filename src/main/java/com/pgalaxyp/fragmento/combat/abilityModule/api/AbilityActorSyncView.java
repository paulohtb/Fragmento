package com.pgalaxyp.fragmento.combat.abilityModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import java.util.*;

public record AbilityActorSyncView(Map<ActorId, ClassId> classes, Set<ActorId> liveActorIds) {
    public static final AbilityActorSyncView EMPTY = new AbilityActorSyncView(Map.of(), Set.of());

    public AbilityActorSyncView {
        classes = Map.copyOf(Objects.requireNonNull(classes));
        liveActorIds = Set.copyOf(Objects.requireNonNull(liveActorIds));
        classes.forEach((k, v) -> { if (k == null || v == null) throw new IllegalArgumentException(); });
        liveActorIds.forEach(id -> { if (id == null) throw new IllegalArgumentException(); });
    }

    public Optional<ClassId> classIdOf(ActorId actorId) {
        return Optional.ofNullable(classes.get(Objects.requireNonNull(actorId)));
    }
}