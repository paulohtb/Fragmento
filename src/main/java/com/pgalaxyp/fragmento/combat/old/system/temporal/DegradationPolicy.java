package com.pgalaxyp.fragmento.combat.old.system.temporal;

public final class DegradationPolicy {

    private int skipCounter;

    public boolean allowHeavyStep(long remainingNanos) {
        if (remainingNanos > 2_000_000L) {
            skipCounter = 0;
            return true;
        }

        skipCounter++;
        return skipCounter % 2 == 0;
    }

    public void reset() {
        skipCounter = 0;
    }
}