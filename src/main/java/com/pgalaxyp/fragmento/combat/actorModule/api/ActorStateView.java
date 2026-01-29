package com.pgalaxyp.fragmento.combat.actorModule.api;

import java.util.Optional;

public interface ActorStateView {
    ActorView actors();
    default Optional<ActorState> findActor(ActorId actorId) { return actors().findActor(actorId); }
}