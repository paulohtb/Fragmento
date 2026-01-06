package com.pgalaxyp.fragmento.rpg.core.domain.event;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.action.InterruptMask;

public record ActionInterruptedEvent(
        long actorId,
        ActionId actionId,
        InterruptMask cause,
        long createdAt
) implements RpgEvent {}