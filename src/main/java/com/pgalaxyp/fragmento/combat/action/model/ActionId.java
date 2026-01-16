package com.pgalaxyp.fragmento.combat.action.model;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public record ActionId(String value) implements Comparable<ActionId> {
    public ActionId { value = IdValidation.normalizedKey(value); }
    @Override public int compareTo(ActionId o) { return value.compareTo(Objects.requireNonNull(o).value); }
}