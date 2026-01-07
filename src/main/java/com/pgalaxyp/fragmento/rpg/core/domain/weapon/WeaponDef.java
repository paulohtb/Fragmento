package com.pgalaxyp.fragmento.rpg.core.domain.weapon;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;

public record WeaponDef(
        String id,
        ActionDef primaryAction
) {
    public WeaponDef {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("WeaponDef.id");
        if (primaryAction == null) throw new IllegalArgumentException("WeaponDef.primaryAction");
    }
}