package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.abilityModule.port.AbilityActorViewPort;
import java.util.*;

public final class AbilityActorIndex implements AbilityActorViewPort {
    private volatile Map<ActorId, ClassId> classes = Map.of();

    void update(Map<ActorId, ClassId> classes) {
        this.classes = Map.copyOf(Objects.requireNonNull(classes));
    }

    @Override public Optional<ClassId> classIdOf(ActorId actorId) {
        return Optional.ofNullable(classes.get(Objects.requireNonNull(actorId)));
    }
}