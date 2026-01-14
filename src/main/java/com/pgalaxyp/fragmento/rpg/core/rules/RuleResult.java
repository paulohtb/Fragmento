package com.pgalaxyp.fragmento.rpg.core.rules;

import com.pgalaxyp.fragmento.rpg.core.events.event.*;
import com.pgalaxyp.fragmento.rpg.core.events.delta.*;
import java.util.*;

public record RuleResult(List<StateDelta> deltas, List<DomainEvent> events) {

    public RuleResult {
        Objects.requireNonNull(deltas, "deltas cannot be null");
        Objects.requireNonNull(events, "events cannot be null");
        deltas = List.copyOf(deltas);
        events = List.copyOf(events);
    }

    public static RuleResult empty() {
        return new RuleResult(List.of(), List.of());
    }
}