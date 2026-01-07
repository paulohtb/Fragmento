package com.pgalaxyp.fragmento.rpg.core.domain.effect;

import java.util.Map;

public record EffectSpec(
        EffectId id,
        Map<String, String> params
) {
    public EffectSpec {
        if (id == null) throw new IllegalArgumentException("EffectSpec.id");
        params = params == null ? Map.of() : Map.copyOf(params);
    }

    public static EffectSpec of(EffectId id) {
        return new EffectSpec(id, Map.of());
    }
}