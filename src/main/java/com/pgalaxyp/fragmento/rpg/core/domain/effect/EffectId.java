package com.pgalaxyp.fragmento.rpg.core.domain.effect;

public record EffectId(String value) {
    public EffectId {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("EffectId.value");
    }
}