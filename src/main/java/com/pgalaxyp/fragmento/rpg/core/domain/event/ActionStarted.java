package com.pgalaxyp.fragmento.rpg.core.domain.event;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;

public record ActionStarted(
        long actorId,
        ActionId actionId,
        long startedAt,
        long endsAt
) implements DomainEvent {
    public ActionStarted {
        if (actionId == null) throw new IllegalArgumentException("ActionStarted.actionId");
    }
}