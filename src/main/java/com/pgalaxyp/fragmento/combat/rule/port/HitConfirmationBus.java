package com.pgalaxyp.fragmento.combat.rule.port;

public interface HitConfirmationBus {
    void beginAwaitingHit(int actionLocalId);
    void endAwaitingHit(int actionLocalId);
}