package com.pgalaxyp.fragmento.combat.host.api;

import com.pgalaxyp.fragmento.combat.core.domain.ids.ActorId;
import java.util.Optional;

public interface LocalActorProvider {
    Optional<ActorId> localActorId();
}