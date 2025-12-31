package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;
import com.pgalaxyp.fragmento.combat.engine.adapter.action.WeaponActionListener;
import com.pgalaxyp.fragmento.combat.rule.port.WeaponActionEmitter;

public final class SimpleWeaponActionEmitter implements WeaponActionEmitter {

    private int seq = 1;
    private final WeaponActionListener listener;

    public SimpleWeaponActionEmitter(WeaponActionListener listener) {
        this.listener = listener;
    }

    @Override
    public int emitAction(ActionDefinition action) {
        int id = seq++;
        if (listener != null) {
            listener.onActionStarted(id);
        }
        return id;
    }

    @Override
    public void cancelAction(int actionLocalId) {
        if (listener != null) {
            listener.onActionCancelled(actionLocalId);
        }
    }
}