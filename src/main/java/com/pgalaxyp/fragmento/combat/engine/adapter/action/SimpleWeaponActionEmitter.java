package com.pgalaxyp.fragmento.combat.engine.adapter.action;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;
import com.pgalaxyp.fragmento.combat.engine.runtime.WeaponActionEmitter;
import java.util.concurrent.atomic.AtomicInteger;

public final class SimpleWeaponActionEmitter implements WeaponActionEmitter {

    private final AtomicInteger seq = new AtomicInteger(1);
    private final WeaponActionListener listener;

    public SimpleWeaponActionEmitter(WeaponActionListener listener) {
        this.listener = listener;
    }

    @Override
    public int emitAction(ActionDefinition action) {
        int id = seq.getAndIncrement();
        if (listener != null) {
            listener.onActionStarted(id, action);
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