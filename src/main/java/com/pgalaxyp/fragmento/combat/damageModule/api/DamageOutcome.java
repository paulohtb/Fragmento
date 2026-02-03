package com.pgalaxyp.fragmento.combat.damageModule.api;

import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import java.util.*;

public record DamageOutcome(List<FrameEvent> events) {
    private static final DamageOutcome EMPTY = new DamageOutcome(List.of());

    public DamageOutcome {
        events = List.copyOf(Objects.requireNonNull(events));
    }

    public static DamageOutcome empty() { return EMPTY; }
}