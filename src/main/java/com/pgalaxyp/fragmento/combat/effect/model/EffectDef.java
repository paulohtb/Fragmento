package com.pgalaxyp.fragmento.combat.effect.model;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.damage.domain.*;

public record EffectDef(EffectId id, DamageSpec damage) {
    public EffectDef {
        if (id == null || damage == null) throw new IllegalArgumentException();
    }

    public static EffectDef of(EffectId id, DamageSpec damage) { return new EffectDef(id, damage); }
}
