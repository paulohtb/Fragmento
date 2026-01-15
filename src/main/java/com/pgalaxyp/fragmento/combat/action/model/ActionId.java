package com.pgalaxyp.fragmento.combat.action.model;

import com.pgalaxyp.fragmento.combat.core.domain.ids.*;

public record ActionId(String value) implements Comparable<ActionId> {

    public ActionId { value = IdValidation.normalizedKey(value); }

    @Override
    public int compareTo(ActionId other) { return value.compareTo(other.value); }
}