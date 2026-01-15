package com.pgalaxyp.fragmento.combat.core.def;

import com.pgalaxyp.fragmento.combat.core.ids.ClassId;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;

public record ClassDef(
        ClassId id,
        WeaponId startingWeaponId
) {
    public ClassDef {
        if (id == null || startingWeaponId == null) {
            throw new IllegalArgumentException();
        }
    }
}