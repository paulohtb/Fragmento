package com.pgalaxyp.fragmento.rpg.core.domain.ids;

public record WeaponId(String value) implements Comparable<WeaponId> {
    public WeaponId {
        value = IdValidation.normalizedKey(value);
    }

    @Override
    public int compareTo(WeaponId other) {
        if (other == null) {
            throw new IllegalArgumentException();
        }
        return value.compareTo(other.value);
    }
}