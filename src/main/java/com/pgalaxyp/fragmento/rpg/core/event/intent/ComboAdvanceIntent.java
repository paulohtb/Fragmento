package com.pgalaxyp.fragmento.rpg.core.event.intent;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;

public record ComboAdvanceIntent(
        ActionId actionId
) implements DomainIntent {
    public ComboAdvanceIntent {
        if (actionId == null) {
            throw new IllegalArgumentException();
        }
    }
}