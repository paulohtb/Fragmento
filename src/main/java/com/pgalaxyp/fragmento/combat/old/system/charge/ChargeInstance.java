package com.pgalaxyp.fragmento.combat.old.system.charge;

import java.util.UUID;

public final class ChargeInstance {

    private final UUID instrumentId;
    private final int max;
    private int value;

    public ChargeInstance(UUID instrumentId, int max) {
        this.instrumentId = instrumentId;
        this.max = Math.max(1, max);
        this.value = 0;
    }

    public UUID instrumentId() {
        return instrumentId;
    }

    public int value() {
        return value;
    }

    public boolean isCharged() {
        return value >= max;
    }

    public void increment() {
        if (value < max) {
            value++;
        }
    }

    public void reset() {
        value = 0;
    }
}