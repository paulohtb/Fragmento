package com.pgalaxyp.fragmento.combat.state.runtime;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

public final class InfusionRuntimeState {

    private boolean armed;
    private CombatTime expiresAt;

    public InfusionRuntimeState() {
        this.armed = false;
        this.expiresAt = CombatTime.ofTicks(0);
    }

    public boolean armed() {
        return armed;
    }

    public void arm(CombatTime expiresAt) {
        this.armed = true;
        this.expiresAt = expiresAt;
    }

    public void consume() {
        this.armed = false;
        this.expiresAt = CombatTime.ofTicks(0);
    }

    public void tick(CombatTime now) {
        if (!armed) {
            return;
        }
        if (now.isAfterOrEqual(expiresAt)) {
            consume();
        }
    }
}