package com.pgalaxyp.fragmento.rpg.core.domain.ids;

public record ActionId(String value) {
    public ActionId {
        value = IdValidation.normalizedKey(value);
    }
}