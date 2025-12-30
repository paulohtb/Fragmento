package com.pgalaxyp.fragmento.combat.integration.flute;

import com.pgalaxyp.fragmento.combat.engine.adapter.hit.HitConfirmationListener;
import com.pgalaxyp.fragmento.combat.engine.runtime.WeaponCombatRuntime;

public final class FluteHitBridge implements HitConfirmationListener {

    private final WeaponCombatRuntime runtime;
    private int awaitingActionId;

    public FluteHitBridge(WeaponCombatRuntime runtime) {
        this.runtime = runtime;
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
        if (actionLocalId == awaitingActionId) {
            runtime.onHitConfirmed(actionLocalId);
        }
    }
}