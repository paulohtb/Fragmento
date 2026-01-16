package com.pgalaxyp.fragmento.combat.core.ids;

import java.util.*;

public record ClassId(String value) implements Comparable<ClassId> {
    public ClassId { value = IdValidation.normalizedKey(value); }
    @Override public int compareTo(ClassId o) { return value.compareTo(Objects.requireNonNull(o).value); }
}