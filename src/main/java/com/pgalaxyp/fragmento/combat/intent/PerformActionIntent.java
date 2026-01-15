package com.pgalaxyp.fragmento.combat.intent;

import com.pgalaxyp.fragmento.combat.combo.api.ComboInput;

public record PerformActionIntent(ComboInput input) implements DomainIntent {
    public PerformActionIntent {
        if (input == null) { throw new IllegalArgumentException(); }
    }
}