package com.pgalaxyp.fragmento.combat.ability.system;

import com.pgalaxyp.fragmento.combat.delta.StateDelta;
import com.pgalaxyp.fragmento.combat.ability.api.AbilityEvent;
import java.util.*;

public record AbilityCombatResult(List<AbilityEvent> events, List<StateDelta> deltas) {
    public AbilityCombatResult {
        events = List.copyOf(Objects.requireNonNull(events));
        deltas = List.copyOf(Objects.requireNonNull(deltas));
    }

    public static AbilityCombatResult empty() { return new AbilityCombatResult(List.of(), List.of()); }
}