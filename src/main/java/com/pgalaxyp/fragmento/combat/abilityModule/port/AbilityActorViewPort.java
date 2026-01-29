package com.pgalaxyp.fragmento.combat.abilityModule.port;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import java.util.Optional;

@FunctionalInterface
public interface AbilityActorViewPort {
    Optional<ClassId> classIdOf(ActorId actorId);
}