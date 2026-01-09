package com.pgalaxyp.fragmento.rpg.core.event.resolution;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.event.query.QueryId;
import java.util.Optional;

public record TargetingQueryResolved(
        QueryId queryId,
        Optional<ActorId> targetActorId
) implements DomainResolution {
    public TargetingQueryResolved {
        if (queryId == null || targetActorId == null) {
            throw new IllegalArgumentException();
        }
    }

    public static TargetingQueryResolved empty(QueryId queryId) {
        return new TargetingQueryResolved(queryId, Optional.empty());
    }
}