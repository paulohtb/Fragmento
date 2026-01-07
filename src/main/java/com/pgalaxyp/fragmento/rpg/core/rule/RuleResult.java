package com.pgalaxyp.fragmento.rpg.core.rule;

import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.state.delta.StateDelta;
import java.util.List;

public record RuleResult(
        boolean consumed,
        List<StateDelta> deltas,
        List<DomainEvent> events
) {
    public RuleResult {
        deltas = deltas == null ? List.of() : List.copyOf(deltas);
        events = events == null ? List.of() : List.copyOf(events);
    }

    public static RuleResult empty() {
        return new RuleResult(false, List.of(), List.of());
    }

    public static RuleResult consumed(List<StateDelta> deltas, List<DomainEvent> events) {
        return new RuleResult(true, deltas, events);
    }
}