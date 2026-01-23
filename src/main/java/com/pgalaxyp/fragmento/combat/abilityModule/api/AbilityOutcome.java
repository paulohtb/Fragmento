package com.pgalaxyp.fragmento.combat.abilityModule.api;

import com.pgalaxyp.fragmento.combat.random.FrameEvent;
import java.util.*;

public record AbilityOutcome(List<FrameEvent> events) {
    public AbilityOutcome { events = List.copyOf(Objects.requireNonNull(events)); }
    public static AbilityOutcome empty() { return new AbilityOutcome(List.of()); }
}