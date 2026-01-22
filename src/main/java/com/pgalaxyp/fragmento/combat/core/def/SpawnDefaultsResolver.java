package com.pgalaxyp.fragmento.combat.core.def;

import com.pgalaxyp.fragmento.combat.actor.ActorId;
import java.util.Optional;

@FunctionalInterface
public interface SpawnDefaultsResolver {
    Optional<SpawnDefaults> resolve(ActorId actorId);
}