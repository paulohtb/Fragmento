package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.content.registry.*;
import com.pgalaxyp.fragmento.combat.core.def.*;

public final class DefaultWeapons {
    public static void register(ContentRegistry registry) {
        registry.weapon(new WeaponDef(DefaultIds.WEAPON_FLUTE));
    }
    private DefaultWeapons() {}
}