package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.damage.domain.*;
import com.pgalaxyp.fragmento.combat.effect.model.EffectDef;
import com.pgalaxyp.fragmento.combat.content.registry.ContentRegistry;

public final class DefaultEffects {
    public static void register(ContentRegistry registry) {
        registry.effect(EffectDef.of(DefaultIds.EFFECT_FLUTE_MAGIC, new DamageSpec(4, DamageType.MAGIC, DamageElement.AIR)));
        registry.effect(EffectDef.of(DefaultIds.EFFECT_FLUTE_FINISHER_MAGIC, new DamageSpec(8, DamageType.MAGIC, DamageElement.AIR)));
    }

    private DefaultEffects() {}
}