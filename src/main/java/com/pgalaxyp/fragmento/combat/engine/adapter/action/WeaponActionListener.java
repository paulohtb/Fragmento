package com.pgalaxyp.fragmento.combat.engine.adapter.action;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;

public interface WeaponActionListener {

    void onActionStarted(int localActionId, ActionDefinition definition);

    void onActionCancelled(int localActionId);
}