package com.pgalaxyp.fragmento.combat.core.domain.def;

import com.pgalaxyp.fragmento.combat.core.domain.ids.ClassId;
import com.pgalaxyp.fragmento.combat.core.domain.ids.WeaponId;

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