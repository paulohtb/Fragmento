package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;

public interface WeaponActionEmitter {
    int emitAction(ActionDefinition action);
    void cancelAction(int actionLocalId);
}