package com.pgalaxyp.fragmento.combat.effectModule.api;

import java.util.Objects;

public record EffectId(String value) implements Comparable<EffectId> {
    public EffectId { Objects.requireNonNull(value); }
    @Override public int compareTo(EffectId id) { return value.compareTo(Objects.requireNonNull(id).value); }
}