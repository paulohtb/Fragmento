package com.pgalaxyp.fragmento.combat.effectModule.api;

import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import java.util.*;

public record EffectOutcome(List<FrameEvent> events) {
    private static final EffectOutcome EMPTY = new EffectOutcome(List.of());

    public EffectOutcome {
        events = List.copyOf(Objects.requireNonNull(events));
    }

    public static EffectOutcome empty() { return EMPTY; }
}