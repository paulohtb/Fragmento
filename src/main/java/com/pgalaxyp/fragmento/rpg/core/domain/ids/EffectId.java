package com.pgalaxyp.fragmento.rpg.core.domain.ids;

public record EffectId(String value) {
    public EffectId {
        value = IdValidation.normalizedKey(value);
    }
}