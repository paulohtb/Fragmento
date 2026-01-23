package com.pgalaxyp.fragmento.combat.abilityModule.api;

import java.util.*;

public record AbilityViewSnapshot(List<AbilitySnapshot> active) {
    public AbilityViewSnapshot { active = List.copyOf(Objects.requireNonNull(active)); }
}