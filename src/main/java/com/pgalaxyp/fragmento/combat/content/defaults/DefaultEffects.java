package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.content.registry.*;
import com.pgalaxyp.fragmento.combat.damage.domain.*;
import com.pgalaxyp.fragmento.combat.effect.model.*;

public final class DefaultEffects {
    public static void register(ContentRegistry registry) {
        var dmg = new DamageSpec(4, DamageType.MAGIC, DamageElement.AIR);
        registry.effect(EffectDef.of(DefaultIds.EFFECT_FLUTE_MAGIC, dmg));
    }
    private DefaultEffects() {}
}
