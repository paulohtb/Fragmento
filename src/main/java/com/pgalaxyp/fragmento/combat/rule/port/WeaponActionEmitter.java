package com.pgalaxyp.fragmento.combat.rule.port;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;

public interface WeaponActionEmitter {
    int emitAction(ActionDefinition action);

    void cancelAction(int actionLocalId);
}