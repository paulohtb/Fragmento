package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.content.registry.*;
import com.pgalaxyp.fragmento.combat.core.def.*;

public final class DefaultClasses {
    public static void register(ContentRegistry registry) {
        registry.defaults(SpawnDefaults.of(DefaultIds.CLASS_BARD, DefaultIds.WEAPON_FLUTE, 20, 20));
    }
    private DefaultClasses() {}
}
