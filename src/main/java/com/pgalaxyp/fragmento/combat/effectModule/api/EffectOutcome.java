package com.pgalaxyp.fragmento.combat.effectModule.api;

import com.pgalaxyp.fragmento.combat.random.FrameEvent;
import java.util.*;

public record EffectOutcome(List<FrameEvent> events) {
    public EffectOutcome { events = List.copyOf(Objects.requireNonNull(events)); }
    public static EffectOutcome empty() { return new EffectOutcome(List.of()); }
}