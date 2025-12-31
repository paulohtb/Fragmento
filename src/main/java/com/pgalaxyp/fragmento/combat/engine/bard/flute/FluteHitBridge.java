package com.pgalaxyp.fragmento.combat.engine.bard.flute;

import com.pgalaxyp.fragmento.combat.engine.adapter.hit.HitConfirmationListener;
import com.pgalaxyp.fragmento.combat.engine.runtime.WeaponHitBridge;
import com.pgalaxyp.fragmento.combat.engine.time.CombatClock;
import com.pgalaxyp.fragmento.combat.rule.runtime.WeaponCombatRuntime;

public final class FluteHitBridge implements HitConfirmationListener {

    private final WeaponHitBridge bridge;

    public FluteHitBridge(
            WeaponCombatRuntime runtime,
            CombatClock clock
    ) {
        this.bridge = new WeaponHitBridge(runtime, clock);
    }

    @Override
    public void onAwaitHit(int actionLocalId) {
        bridge.onAwaitHit(actionLocalId);
    }

    @Override
    public void onStopAwaitHit(int actionLocalId) {
        bridge.onStopAwaitHit(actionLocalId);
    }

    @Override
    public void onHitConfirmed(int actionLocalId) {
        bridge.onHitConfirmed(actionLocalId);
    }
}