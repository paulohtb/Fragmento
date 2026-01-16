package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.content.registry.ContentRegistry;
import com.pgalaxyp.fragmento.combat.cycle.model.*;

public final class DefaultCycles {
    public static void register(ContentRegistry registry) {
        registry.cycles().register(DefaultWeapons.FLUTE, new ActionCycleDef(DefaultCombos.BASIC, DefaultActions.MAGIC_BASIC));
    }
    private DefaultCycles() {}
}