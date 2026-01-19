package com.pgalaxyp.fragmento.combat.core.ids;

import java.util.*;

public record BuffId(String value) implements Comparable<BuffId> {
    public BuffId { value = IdValidation.normalizedKey(value); }
    @Override public int compareTo(BuffId o) { return value.compareTo(Objects.requireNonNull(o).value); }
}