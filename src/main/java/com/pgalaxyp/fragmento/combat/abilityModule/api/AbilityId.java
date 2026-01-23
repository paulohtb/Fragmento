package com.pgalaxyp.fragmento.combat.abilityModule.api;

import java.util.Objects;

public record AbilityId(String value) implements Comparable<AbilityId> {
    public AbilityId { Objects.requireNonNull(value); }
    @Override public int compareTo(AbilityId id) { return value.compareTo(Objects.requireNonNull(id).value); }
}