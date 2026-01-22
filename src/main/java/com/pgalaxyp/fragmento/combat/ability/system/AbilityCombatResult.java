package com.pgalaxyp.fragmento.combat.ability.system;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityEvent;
import java.util.*;

public record AbilityCombatResult(List<AbilityEvent> events) {

    public AbilityCombatResult {
        events = List.copyOf(Objects.requireNonNull(events));
    }

    public static AbilityCombatResult empty() {
        return new AbilityCombatResult(List.of());
    }
}