package com.pgalaxyp.fragmento.rpg.core.domain.weapon;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;

public record WeaponDef(
        String id,
        ActionDef primaryAction
) {}