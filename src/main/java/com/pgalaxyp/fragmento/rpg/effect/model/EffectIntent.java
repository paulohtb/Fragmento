package com.pgalaxyp.fragmento.rpg.effect.model;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;

public record EffectIntent(EffectId effectId) {
    public EffectIntent { if (effectId == null) throw new IllegalArgumentException(); }
    public static EffectIntent of(EffectId effectId) { return new EffectIntent(effectId); }
}