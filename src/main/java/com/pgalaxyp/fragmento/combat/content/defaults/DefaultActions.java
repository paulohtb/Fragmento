package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.action.model.*;
import com.pgalaxyp.fragmento.combat.content.registry.*;
import com.pgalaxyp.fragmento.combat.effect.model.*;

public final class DefaultActions {
    public static void register(ContentRegistry registry) {
        registry.action(new ActionDef(DefaultIds.ACTION_FLUTE_CAST, new InstantActionPlan(EffectIntent.of(DefaultIds.EFFECT_FLUTE_MAGIC))));
    }
    private DefaultActions() {}
}