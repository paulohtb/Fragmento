package com.pgalaxyp.fragmento.combat.intent;

public record PerformActionIntent(ComboInput input) implements DomainIntent {
    public PerformActionIntent {
        if (input == null) { throw new IllegalArgumentException(); }
    }
}