package com.pgalaxyp.fragmento.combat.classModule.api;

import java.util.Objects;

public record ClassId(String value) implements Comparable<ClassId> {
    public ClassId { Objects.requireNonNull(value); }
    @Override public int compareTo(ClassId id) { return value.compareTo(Objects.requireNonNull(id).value); }
}