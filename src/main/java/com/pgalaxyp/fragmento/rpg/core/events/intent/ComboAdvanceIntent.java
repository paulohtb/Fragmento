package com.pgalaxyp.fragmento.rpg.core.events.intent;

import com.pgalaxyp.fragmento.rpg.action.key.*;

public record ComboAdvanceIntent(ActionKey actionKey, int stepIndex) implements DomainIntent {

    public ComboAdvanceIntent {
        if (actionKey == null) {
            throw new IllegalArgumentException();
        }
        if (stepIndex < 0) {
            throw new IllegalArgumentException();
        }
    }
}