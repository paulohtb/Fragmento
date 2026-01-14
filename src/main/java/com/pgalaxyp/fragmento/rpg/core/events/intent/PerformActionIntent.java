package com.pgalaxyp.fragmento.rpg.core.events.intent;

import com.pgalaxyp.fragmento.rpg.combo.api.ComboInput;

public record PerformActionIntent(
        ComboInput input
) implements DomainIntent {

    public PerformActionIntent {
        if (input == null) {
            throw new IllegalArgumentException();
        }
    }
}