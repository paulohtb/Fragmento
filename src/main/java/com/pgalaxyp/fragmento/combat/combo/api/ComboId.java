package com.pgalaxyp.fragmento.combat.combo.api;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public record ComboId(String value) implements Comparable<ComboId> {
    public ComboId { value = IdValidation.normalizedKey(value); }
    @Override public int compareTo(ComboId o) { return value.compareTo(Objects.requireNonNull(o).value); }
}
