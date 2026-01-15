package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.content.registry.ContentRegistry;
import com.pgalaxyp.fragmento.combat.cycle.model.*;
import com.pgalaxyp.fragmento.combat.action.model.*;

public final class DefaultCycles {
    public static void register(ContentRegistry registry) {
        registry.cycles().register(
                DefaultWeapons.FLUTE,
                new ActionCycleDef(DefaultCombos.BASIC, new ActionId("action.magic.basic"))
        );
    }
    private DefaultCycles() {}
}