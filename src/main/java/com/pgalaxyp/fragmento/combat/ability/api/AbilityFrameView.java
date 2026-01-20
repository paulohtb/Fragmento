package com.pgalaxyp.fragmento.combat.ability.api;

import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import java.util.*;

public record AbilityFrameView(List<AbilitySnapshot> active, NavigableMap<ActorId, AbilityExecutionState> execution) {
    public AbilityFrameView {
        active = List.copyOf(Objects.requireNonNull(active));
        execution = Collections.unmodifiableNavigableMap(new TreeMap<>(Objects.requireNonNull(execution)));
    }
}