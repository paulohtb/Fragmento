package com.pgalaxyp.fragmento.rpg.core.domain.ids;

public record ClassId(String value) implements Comparable<ClassId> {
    public ClassId {
        value = IdValidation.normalizedKey(value);
    }

    @Override
    public int compareTo(ClassId other) {
        if (other == null) {
            throw new IllegalArgumentException();
        }
        return value.compareTo(other.value);
    }
}