package com.pgalaxyp.fragmento.rpg.core.domain.weapon;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboDef;

public record WeaponDef(
        WeaponId id,
        ActionDef action,
        ComboDef combo
) {}