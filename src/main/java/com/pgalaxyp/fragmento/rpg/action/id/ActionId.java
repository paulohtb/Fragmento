package com.pgalaxyp.fragmento.rpg.action.id;

import java.util.*;

public record ActionId(String value) implements Comparable<ActionId> {

    public ActionId {
        Objects.requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int compareTo(ActionId other) {
        return value.compareTo(other.value);
    }
}