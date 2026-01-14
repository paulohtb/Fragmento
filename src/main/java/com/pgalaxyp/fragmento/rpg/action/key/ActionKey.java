package com.pgalaxyp.fragmento.rpg.action.key;

import java.util.*;

public record ActionKey(String value) implements Comparable<ActionKey> {

    public ActionKey {
        Objects.requireNonNull(value, "value cannot be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException("value cannot be blank");
        }
    }

    @Override
    public int compareTo(ActionKey other) {
        Objects.requireNonNull(other, "other cannot be null");

        return this.value.compareTo(other.value);
    }
}