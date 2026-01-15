package com.pgalaxyp.fragmento.combat.combo.model;

import com.pgalaxyp.fragmento.combat.core.ids.*;

public record ComboId(String value) implements Comparable<ComboId> {

    public ComboId { value = IdValidation.normalizedKey(value); }

    @Override
    public int compareTo(ComboId comboId) { return value.compareTo(comboId.value);}
}