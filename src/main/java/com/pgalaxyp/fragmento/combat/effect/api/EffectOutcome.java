package com.pgalaxyp.fragmento.combat.effect.api;

import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.*;

public record EffectOutcome(List<DomainEvent> events) {
    public EffectOutcome {
        events = List.copyOf(Objects.requireNonNull(events));
    }

    public static EffectOutcome empty() {
        return new EffectOutcome(List.of());
    }
}