package com.pgalaxyp.fragmento.rpg.core.state.action;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionPriority;
import com.pgalaxyp.fragmento.rpg.core.domain.action.InterruptMask;

import java.util.Set;

public record ActionState(
        ActionId actionId,
        ActionPriority priority,
        long startedAt,
        long endsAt,
        Set<InterruptMask> interruptMask
) {
    public ActionState {
        interruptMask = interruptMask == null ? Set.of() : Set.copyOf(interruptMask);
    }

    public boolean isActiveAt(long now) {
        return now >= startedAt && now < endsAt;
    }

    public boolean canBeInterruptedBy(InterruptMask cause) {
        return cause != null && interruptMask.contains(cause);
    }
}