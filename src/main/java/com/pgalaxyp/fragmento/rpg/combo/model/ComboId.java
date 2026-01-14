package com.pgalaxyp.fragmento.rpg.combo.model;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;

public record ComboId(String value) implements Comparable<ComboId> {

    public ComboId { value = IdValidation.normalizedKey(value); }

    @Override
    public int compareTo(ComboId comboId) { return value.compareTo(comboId.value);}
}