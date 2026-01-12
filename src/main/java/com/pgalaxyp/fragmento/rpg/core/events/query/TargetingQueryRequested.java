package com.pgalaxyp.fragmento.rpg.core.events.query;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.QueryId;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.TargetingSpec;

public record TargetingQueryRequested(
        QueryId queryId,
        ActorId sourceActorId,
        ActionId actionId,
        int stepIndex,
        TargetingSpec spec
) implements ExternalQueryEvent {
    public TargetingQueryRequested {
        if (queryId == null || sourceActorId == null || actionId == null || spec == null) {
            throw new IllegalArgumentException();
        }
        if (stepIndex < 0) {
            throw new IllegalArgumentException();
        }
    }
}