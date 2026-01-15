package com.pgalaxyp.fragmento.combat.effect.model;

import com.pgalaxyp.fragmento.combat.delta.*;
import com.pgalaxyp.fragmento.combat.event.*;
import java.util.*;

public record EffectOutcome(List<StateDelta> deltas, List<DomainEvent> events) {
    public EffectOutcome {
        deltas = List.copyOf(Objects.requireNonNull(deltas));
        events = List.copyOf(Objects.requireNonNull(events));
    }

    public static EffectOutcome empty() { return new EffectOutcome(List.of(), List.of()); }
}