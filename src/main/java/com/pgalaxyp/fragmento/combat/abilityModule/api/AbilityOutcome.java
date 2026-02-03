package com.pgalaxyp.fragmento.combat.abilityModule.api;

import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import java.util.*;

public record AbilityOutcome(List<FrameEvent> events) {
    private static final AbilityOutcome EMPTY = new AbilityOutcome(List.of());

    public AbilityOutcome {
        events = List.copyOf(Objects.requireNonNull(events));
    }

    public static AbilityOutcome empty() { return EMPTY; }
}