package com.pgalaxyp.fragmento.rpg.core.domain.def;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ClassId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;

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