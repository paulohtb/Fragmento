package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.content.registry.*;

public final class DefaultCycles {
    public static void register(ContentRegistry registry) {
        registry.cycle(DefaultIds.COMBO_FLUTE_BASIC, DefaultIds.WEAPON_FLUTE, DefaultIds.ACTION_FLUTE_CAST);
    }
    private DefaultCycles() {}
}