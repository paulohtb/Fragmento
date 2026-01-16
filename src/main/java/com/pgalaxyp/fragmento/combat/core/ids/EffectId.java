package com.pgalaxyp.fragmento.combat.core.ids;

import java.util.*;

public record EffectId(String value) implements Comparable<EffectId> {
    public EffectId { value = IdValidation.normalizedKey(value); }
    @Override public int compareTo(EffectId o) { return value.compareTo(Objects.requireNonNull(o).value); }
}