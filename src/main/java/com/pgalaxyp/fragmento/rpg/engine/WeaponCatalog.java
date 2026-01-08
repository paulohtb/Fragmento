package com.pgalaxyp.fragmento.rpg.engine;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.weapon.WeaponDef;

public interface WeaponCatalog {
    WeaponDef findByActionId(ActionId actionId);
}