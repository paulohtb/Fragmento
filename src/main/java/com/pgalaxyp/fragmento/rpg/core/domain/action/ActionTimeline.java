package com.pgalaxyp.fragmento.rpg.core.domain.action;

public record ActionTimeline(
        long windupMillis,
        long activeMillis,
        long recoveryMillis
) {
    public ActionTimeline {
        windupMillis = Math.max(0L, windupMillis);
        activeMillis = Math.max(0L, activeMillis);
        recoveryMillis = Math.max(0L, recoveryMillis);
    }

    public long totalMillis() {
        return windupMillis + activeMillis + recoveryMillis;
    }
}