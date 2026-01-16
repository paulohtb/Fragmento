package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.action.model.*;
import com.pgalaxyp.fragmento.combat.content.registry.ContentRegistry;
import com.pgalaxyp.fragmento.combat.effect.model.*;

public final class DefaultActions {
    public static final ActionId MAGIC_BASIC = new ActionId("action.magic.basic");

    public static void register(ContentRegistry registry) {
        registry.action(new ActionDef(MAGIC_BASIC, new InstantActionPlan(EffectIntent.of(DefaultEffects.MAGIC))));
    }

    private DefaultActions() {}
}