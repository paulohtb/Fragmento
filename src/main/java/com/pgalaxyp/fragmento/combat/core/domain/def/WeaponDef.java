package com.pgalaxyp.fragmento.combat.core.domain.def;

import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import com.pgalaxyp.fragmento.combat.core.domain.spec.*;

public record WeaponDef(WeaponId id, WeaponSpec spec) {

    public WeaponDef {
        if (id == null || spec == null) throw new IllegalArgumentException();
    }

    public static WeaponDef empty(WeaponId id) { return new WeaponDef(id, WeaponSpec.empty()); }
}