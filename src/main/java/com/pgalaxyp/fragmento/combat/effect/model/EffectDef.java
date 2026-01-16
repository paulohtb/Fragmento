package com.pgalaxyp.fragmento.combat.effect.model;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.damage.domain.*;

public record EffectDef(EffectId id, DamageSpec damage, int visualLifetimeFrames) {
    private static final int DEFAULT_VISUAL_LIFETIME_FRAMES = 12;

    public EffectDef {
        if (id == null || damage == null) throw new IllegalArgumentException();
        if (visualLifetimeFrames < 0) throw new IllegalArgumentException();
    }

    public static EffectDef of(EffectId id, DamageSpec damage) {
        return new EffectDef(id, damage, 0);
    }

    public static EffectDef withVisual(EffectId id, DamageSpec damage) {
        return new EffectDef(id, damage, DEFAULT_VISUAL_LIFETIME_FRAMES);
    }

    public static EffectDef withVisual(EffectId id, DamageSpec damage, int lifetimeFrames) {
        return new EffectDef(id, damage, lifetimeFrames);
    }

    public boolean hasVisual() { return visualLifetimeFrames > 0; }
}
