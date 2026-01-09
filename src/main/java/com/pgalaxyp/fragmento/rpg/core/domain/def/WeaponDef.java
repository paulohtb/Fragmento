package com.pgalaxyp.fragmento.rpg.core.domain.def;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.WeaponSpec;

public record WeaponDef(
        WeaponId id,
        WeaponSpec spec,
        ActionId actionId
) {
    public WeaponDef {
        if (id == null || spec == null || actionId == null) {
            throw new IllegalArgumentException();
        }
    }
}