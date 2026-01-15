package com.pgalaxyp.fragmento.combat.core.def;

import com.pgalaxyp.fragmento.combat.core.ids.*;

public record WeaponDef(WeaponId id) {
    public WeaponDef {
        if (id == null) throw new IllegalArgumentException();
    }

    public static WeaponDef of(WeaponId id) { return new WeaponDef(id); }
}