package com.pgalaxyp.fragmento.rpg.core.domain.def;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.EffectId;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.DamageSpec;

public record EffectDef(
        EffectId id,
        DamageSpec damage
) {
    public EffectDef {
        if (id == null || damage == null) {
            throw new IllegalArgumentException();
        }
    }
}