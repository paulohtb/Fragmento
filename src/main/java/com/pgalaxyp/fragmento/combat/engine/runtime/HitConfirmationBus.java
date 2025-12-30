package com.pgalaxyp.fragmento.combat.engine.runtime;

public interface HitConfirmationBus {
    void beginAwaitingHit(int actionLocalId);
    void endAwaitingHit(int actionLocalId);
}