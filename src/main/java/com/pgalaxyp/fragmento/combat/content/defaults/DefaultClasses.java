package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.content.registry.*;

public final class DefaultClasses {
    public static void register(ContentRegistry registry) {
        registry.defaults(SpawnDefaults.of(DefaultIds.CLASS_DEFAULT, DefaultIds.WEAPON_FLUTE, 20, 20));
    }
    private DefaultClasses() {}
}