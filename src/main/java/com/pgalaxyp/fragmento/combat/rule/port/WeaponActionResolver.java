package com.pgalaxyp.fragmento.combat.rule.port;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;
import com.pgalaxyp.fragmento.combat.domain.id.ActionId;

public interface WeaponActionResolver {

    ActionDefinition resolve(ActionId actionId);
}