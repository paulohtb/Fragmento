package com.pgalaxyp.fragmento.combat.effect.model;

import com.pgalaxyp.fragmento.combat.core.domain.ids.*;

public record EffectIntent(EffectId effectId) {
    public EffectIntent { if (effectId == null) throw new IllegalArgumentException(); }
    public static EffectIntent of(EffectId effectId) { return new EffectIntent(effectId); }
}