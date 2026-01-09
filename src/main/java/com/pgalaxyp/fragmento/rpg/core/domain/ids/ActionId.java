package com.pgalaxyp.fragmento.rpg.core.domain.ids;

public record ActionId(String value) implements Comparable<ActionId> {
    public ActionId {
        value = IdValidation.normalizedKey(value);
    }

    @Override
    public int compareTo(ActionId other) {
        if (other == null) {
            throw new IllegalArgumentException();
        }
        return value.compareTo(other.value);
    }
}