package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.def.*;
import com.pgalaxyp.fragmento.combat.content.registry.*;

public final class DefaultWeapons {
    public static final WeaponId FLUTE = new WeaponId("weapon.flute");
    public static void register(ContentRegistry registry) {
        registry.weapon(WeaponDef.of(FLUTE));
    }
    private DefaultWeapons() {}
}