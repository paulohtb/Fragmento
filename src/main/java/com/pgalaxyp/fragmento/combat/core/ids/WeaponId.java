package com.pgalaxyp.fragmento.combat.core.ids;

import java.util.*;

public record WeaponId(String value) implements Comparable<WeaponId> {
    public WeaponId { value = IdValidation.normalizedKey(value); }
    @Override public int compareTo(WeaponId o) { return value.compareTo(Objects.requireNonNull(o).value); }
}