package com.pgalaxyp.fragmento.combat.effect.model;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.damage.domain.*;

public record EffectDef(EffectId id, DamageSpec damage) {
    public static EffectDef withVisual(EffectId id, DamageSpec damage) {
        if (id == null || damage == null) { throw new IllegalArgumentException(); }
        return new EffectDef(id, damage);
    }
}