package com.pgalaxyp.fragmento.rpg.core.event.query;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.TargetingSpec;

public record TargetingQueryRequested(
        QueryId queryId,
        ActorId sourceActorId,
        TargetingSpec spec
) implements ExternalQueryEvent {
    public TargetingQueryRequested {
        if (queryId == null || sourceActorId == null || spec == null) {
            throw new IllegalArgumentException();
        }
    }
}