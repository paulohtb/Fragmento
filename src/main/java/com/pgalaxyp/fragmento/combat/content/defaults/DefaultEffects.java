package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.content.registry.ContentRegistry;
import com.pgalaxyp.fragmento.combat.core.def.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.damage.domain.*;

public final class DefaultEffects {
    public static final EffectId MAGIC = new EffectId("effect.magic.basic");
    public static void register(ContentRegistry registry) {
        registry.effect(EffectDef.withVisual(
                        MAGIC,
                        new DamageSpec(2, DamageType.MAGIC, DamageElement.AIR)));
    }
    private DefaultEffects() {}
}