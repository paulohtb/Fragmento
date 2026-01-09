package com.pgalaxyp.fragmento.rpg.core.domain.ids;

public record ClassId(String value) {
    public ClassId {
        value = IdValidation.normalizedKey(value);
    }
}