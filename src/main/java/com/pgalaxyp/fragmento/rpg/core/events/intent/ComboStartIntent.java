package com.pgalaxyp.fragmento.rpg.core.events.intent;

import com.pgalaxyp.fragmento.rpg.action.key.*;

public record ComboStartIntent(ActionKey actionKey) implements DomainIntent {

    public ComboStartIntent {
        if (actionKey == null) {
            throw new IllegalArgumentException();
        }
    }
}