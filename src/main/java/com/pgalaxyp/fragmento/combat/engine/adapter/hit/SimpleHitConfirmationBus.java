package com.pgalaxyp.fragmento.combat.engine.adapter.hit;

import com.pgalaxyp.fragmento.combat.rule.port.HitConfirmationBus;

import java.util.HashSet;
import java.util.Set;

public final class SimpleHitConfirmationBus implements HitConfirmationBus {

    private final Set<Integer> awaiting = new HashSet<>();
    private HitConfirmationListener listener;

    public void bind(HitConfirmationListener listener) {
        this.listener = listener;
    }

    @Override
    public void beginAwaitingHit(int actionLocalId) {
        if (listener == null || awaiting.contains(actionLocalId)) {
            return;
        }
        awaiting.add(actionLocalId);
        listener.onAwaitHit(actionLocalId);
    }

    @Override
    public void endAwaitingHit(int actionLocalId) {
        if (listener == null || !awaiting.remove(actionLocalId)) {
            return;
        }
        listener.onStopAwaitHit(actionLocalId);
    }
}