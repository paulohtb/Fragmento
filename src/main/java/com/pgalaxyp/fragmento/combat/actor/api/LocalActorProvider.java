package com.pgalaxyp.fragmento.combat.actor.api;

import java.util.Optional;

public interface LocalActorProvider {
    Optional<ActorId> localActorId();
}