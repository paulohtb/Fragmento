package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.content.catalog.*;
import com.pgalaxyp.fragmento.combat.damage.domain.*;
import com.pgalaxyp.fragmento.combat.effect.model.*;
import java.util.*;

final class DefaultEffects {

    static EffectCatalog create() {
        EffectDef def = new EffectDef(
                DefaultIds.EFFECT_FLUTE_MAGIC,
                new DamageSpec(4, DamageType.MAGIC, DamageElement.AIR)
        );

        return new EffectCatalog(Map.of(def.id(), def));
    }
}