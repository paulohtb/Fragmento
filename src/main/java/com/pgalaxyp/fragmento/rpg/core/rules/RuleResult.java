package com.pgalaxyp.fragmento.rpg.core.rules;

import com.pgalaxyp.fragmento.rpg.core.events.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.events.event.DomainEvent;
import java.util.List;

public record RuleResult(
        List<StateDelta> deltas,
        List<DomainEvent> events
) {
    public RuleResult {
        if (deltas == null || events == null) {
            throw new IllegalArgumentException();
        }
        deltas = List.copyOf(deltas);
        events = List.copyOf(events);
    }

    public static RuleResult empty() {
        return new RuleResult(List.of(), List.of());
    }
}