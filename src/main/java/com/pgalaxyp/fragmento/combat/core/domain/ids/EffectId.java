package com.pgalaxyp.fragmento.combat.core.domain.ids;

public record EffectId(String value) implements Comparable<EffectId> {
    public EffectId {
        value = IdValidation.normalizedKey(value);
    }

    @Override
    public int compareTo(EffectId other) {
        if (other == null) {
            throw new IllegalArgumentException();
        }
        return value.compareTo(other.value);
    }
}