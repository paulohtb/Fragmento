package com.pgalaxyp.fragmento.combat.actor;

import java.util.Optional;

public interface LocalActorProvider {
    Optional<ActorId> localActorId();
}