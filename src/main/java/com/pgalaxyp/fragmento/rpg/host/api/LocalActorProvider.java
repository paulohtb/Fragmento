package com.pgalaxyp.fragmento.rpg.host.api;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import java.util.Optional;

public interface LocalActorProvider {
    Optional<ActorId> localActorId();
}