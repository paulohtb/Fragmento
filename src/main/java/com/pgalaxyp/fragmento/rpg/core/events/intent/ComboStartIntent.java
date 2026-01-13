package com.pgalaxyp.fragmento.rpg.core.events.intent;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;

public record ComboStartIntent(
        ActionId actionId
) implements DomainIntent {
    public ComboStartIntent {
        if (actionId == null) {
            throw new IllegalArgumentException();
        }
    }
}