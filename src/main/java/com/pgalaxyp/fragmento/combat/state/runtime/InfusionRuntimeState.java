package com.pgalaxyp.fragmento.combat.state.runtime;

public final class InfusionRuntimeState {

    private boolean armed;

    public InfusionRuntimeState() {
        this.armed = false;
    }

    public boolean isArmed() {
        return armed;
    }

    public void arm() {
        this.armed = true;
    }

    public boolean consumeIfArmed() {
        if (!armed) {
            return false;
        }
        armed = false;
        return true;
    }
}