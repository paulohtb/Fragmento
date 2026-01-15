package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.content.registry.ContentRegistry;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.combo.model.*;

public final class DefaultCombos {
    public static final ComboId BASIC = new ComboId("combo.basic.primary");
    public static void register(ContentRegistry registry) {
        WeaponId weapon = DefaultWeapons.FLUTE;
        ComboPattern pattern = new ComboPattern(java.util.List.of(new ComboStep(0, ComboInput.PRIMARY)));
        registry.combos().register(weapon, BASIC, pattern);
    }
    private DefaultCombos() {}
}