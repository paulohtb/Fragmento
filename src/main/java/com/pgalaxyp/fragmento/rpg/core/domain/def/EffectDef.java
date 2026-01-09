package com.pgalaxyp.fragmento.rpg.core.domain.def;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.EffectId;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.DamageSpec;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.EffectVisualSpec;
import java.util.Optional;

public record EffectDef(
        EffectId id,
        DamageSpec damage,
        Optional<EffectVisualSpec> visual
) {
    public EffectDef {
        if (id == null || damage == null || visual == null) {
            throw new IllegalArgumentException();
        }
        if (visual.isPresent() && visual.get() == null) {
            throw new IllegalArgumentException();
        }
        visual = visual.isPresent() ? Optional.of(visual.get()) : Optional.empty();
    }

    public EffectDef(EffectId id, DamageSpec damage) {
        this(id, damage, Optional.empty());
    }

    public static EffectDef withVisual(EffectId id, DamageSpec damage, EffectVisualSpec visual) {
        if (visual == null) {
            throw new IllegalArgumentException();
        }
        return new EffectDef(id, damage, Optional.of(visual));
    }
}
