package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.engine.adapter.hit.HitConfirmationListener;
import com.pgalaxyp.fragmento.combat.engine.time.CombatClock;
import com.pgalaxyp.fragmento.combat.rule.runtime.WeaponCombatRuntime;

public final class WeaponHitBridge implements HitConfirmationListener {

    private final WeaponCombatRuntime runtime;
    private final CombatClock clock;
    private int awaitingActionId;

    public WeaponHitBridge(
            WeaponCombatRuntime runtime,
            CombatClock clock
    ) {
        this.runtime = runtime;
        this.clock = clock;
    }

    @Override
    public void onAwaitHit(int actionLocalId) {
        this.awaitingActionId = actionLocalId;
    }

    @Override
    public void onStopAwaitHit(int actionLocalId) {
        if (awaitingActionId == actionLocalId) {
            awaitingActionId = 0;
        }
    }

    @Override
    public void onHitConfirmed(int actionLocalId) {
        if (actionLocalId != awaitingActionId) {
            return;
        }

        awaitingActionId = 0;
        runtime.onHitConfirmed(actionLocalId, clock.now());
    }
}