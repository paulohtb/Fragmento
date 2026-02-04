package com.pgalaxyp.fragmento.combat.abilityModule.api;

import java.util.*;

public record AbilityViewSnapshot(List<AbilitySnapshot> active) {
    public static final AbilityViewSnapshot EMPTY = new AbilityViewSnapshot(List.of());
    public AbilityViewSnapshot { active = List.copyOf(Objects.requireNonNull(active)); }
    public static AbilityViewSnapshot empty() { return EMPTY; }
}