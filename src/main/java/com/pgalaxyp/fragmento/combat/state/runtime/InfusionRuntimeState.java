package com.pgalaxyp.fragmento.combat.state.runtime;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

public final class InfusionRuntimeState {

    private boolean armed;
    private CombatTime armedUntil;

    public InfusionRuntimeState() {
        this.armed = false;
        this.armedUntil = null;
    }

    public boolean armed() {
        return armed;
    }

    public CombatTime armedUntil() {
        return armedUntil;
    }

    public void arm() {
        this.armed = true;
    }

    public void arm(CombatTime until) {
        this.armed = true;
        this.armedUntil = until;
    }

    public void expiresAt(CombatTime until) {
        this.armedUntil = until;
    }

    public boolean expired(CombatTime now) {
        if (!armed) {
            return true;
        }
        if (armedUntil == null || now == null) {
            return false;
        }
        return now.ticks() >= armedUntil.ticks();
    }

    public void consume() {
        this.armed = false;
        this.armedUntil = null;
    }
}