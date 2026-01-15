package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.content.registry.ContentRegistry;
import com.pgalaxyp.fragmento.combat.action.model.*;
import com.pgalaxyp.fragmento.combat.effect.model.*;

public final class DefaultActions {
    public static void register(ContentRegistry registry) {
        ActionId id = new ActionId("action.magic.basic");
        registry.action(new ActionDef(id, new InstantActionPlan(EffectIntent.of(DefaultEffects.MAGIC))));
    }
    private DefaultActions() {}
}