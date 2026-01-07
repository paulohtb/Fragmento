package com.pgalaxyp.fragmento.rpg.core.domain.event;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;

public record ComboAdvanced(
        long actorId,
        ActionId actionId,
        int nextIndex
) implements DomainEvent {
    public ComboAdvanced {
        if (actionId == null) throw new IllegalArgumentException("ComboAdvanced.actionId");
        nextIndex = Math.max(0, nextIndex);
    }
}