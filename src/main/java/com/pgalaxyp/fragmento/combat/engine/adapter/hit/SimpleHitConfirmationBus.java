package com.pgalaxyp.fragmento.combat.engine.adapter.hit;

import com.pgalaxyp.fragmento.combat.engine.runtime.HitConfirmationBus;

public final class SimpleHitConfirmationBus implements HitConfirmationBus {

    private HitConfirmationListener listener;

    public void bind(HitConfirmationListener listener) {
        this.listener = listener;
    }

    @Override
    public void beginAwaitingHit(int actionLocalId) {
        if (listener != null) {
            listener.onAwaitHit(actionLocalId);
        }
    }

    @Override
    public void endAwaitingHit(int actionLocalId) {
        if (listener != null) {
            listener.onStopAwaitHit(actionLocalId);
        }
    }
}