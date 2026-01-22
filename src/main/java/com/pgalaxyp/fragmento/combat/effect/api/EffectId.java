package com.pgalaxyp.fragmento.combat.effect.api;

import com.pgalaxyp.fragmento.combat.core.ids.IdValidation;

import java.util.*;

public record EffectId(String value) implements Comparable<EffectId> {
    public EffectId { value = IdValidation.normalizedKey(value); }
    @Override public int compareTo(EffectId o) { return value.compareTo(Objects.requireNonNull(o).value); }
}