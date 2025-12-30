package com.pgalaxyp.fragmento.combat.engine.adapter.hit;

public interface HitConfirmationListener {

    void onAwaitHit(int actionLocalId);

    void onStopAwaitHit(int actionLocalId);

    void onHitConfirmed(int actionLocalId);
}