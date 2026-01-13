package com.pgalaxyp.fragmento.rpg.core.events.intent;

public record PerformActionIntent(
        int stepIndex
) implements DomainIntent {

    public PerformActionIntent {
        if (stepIndex < 0) {
            throw new IllegalArgumentException();
        }
    }
}