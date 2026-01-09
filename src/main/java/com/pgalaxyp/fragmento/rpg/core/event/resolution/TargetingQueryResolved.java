package com.pgalaxyp.fragmento.rpg.core.event.resolution;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.QueryId;
import java.util.List;

public record TargetingQueryResolved(
        QueryId queryId,
        List<TargetingCandidate> candidates
) implements DomainResolution {
    public TargetingQueryResolved {
        if (queryId == null || candidates == null) {
            throw new IllegalArgumentException();
        }
        for (TargetingCandidate c : candidates) {
            if (c == null) {
                throw new IllegalArgumentException();
            }
        }
        candidates = List.copyOf(candidates);
    }

    public static TargetingQueryResolved empty(QueryId queryId) {
        return new TargetingQueryResolved(queryId, List.of());
    }
}