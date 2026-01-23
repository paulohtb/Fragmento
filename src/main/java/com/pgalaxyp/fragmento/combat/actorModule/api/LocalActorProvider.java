package com.pgalaxyp.fragmento.combat.actorModule.api;

import java.util.Optional;

public interface LocalActorProvider {
    Optional<ActorId> localActorId();
}