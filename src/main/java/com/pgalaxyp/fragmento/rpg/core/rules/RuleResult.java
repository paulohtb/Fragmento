package com.pgalaxyp.fragmento.rpg.core.rules;

import com.pgalaxyp.fragmento.rpg.core.event.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.event.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.event.query.ExternalQueryEvent;
import java.util.List;

public record RuleResult(
        List<StateDelta> deltas,
        List<DomainEvent> events,
        List<ExternalQueryEvent> queries
) {
    public RuleResult {
        if (deltas == null || events == null || queries == null) {
            throw new IllegalArgumentException();
        }
        deltas = List.copyOf(deltas);
        events = List.copyOf(events);
        queries = List.copyOf(queries);
    }

    public static RuleResult empty() {
        return new RuleResult(List.of(), List.of(), List.of());
    }
}