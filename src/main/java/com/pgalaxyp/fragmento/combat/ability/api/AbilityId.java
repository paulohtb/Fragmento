package com.pgalaxyp.fragmento.combat.ability.api;

import com.pgalaxyp.fragmento.combat.core.ids.IdValidation;
import java.util.*;

public record AbilityId(String value) implements Comparable<AbilityId> {
    public AbilityId { value = IdValidation.normalizedKey(value); }
    @Override public int compareTo(AbilityId o) { return value.compareTo(Objects.requireNonNull(o).value); }
}
