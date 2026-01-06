package com.pgalaxyp.fragmento.rpg.core.state.action;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionPriority;
import com.pgalaxyp.fragmento.rpg.core.domain.action.InterruptMask;
import java.util.EnumSet;

public record ActionState(
        ActionId actionId,
        ActionPriority priority,
        long startedAt,
        long endsAt,
        EnumSet<InterruptMask> interruptMask
) {
    public boolean isActiveAt(long now) {
        return now < endsAt;
    }

    public boolean canBeInterruptedBy(InterruptMask cause) {
        if (cause == null) return false;
        if (interruptMask == null || interruptMask.isEmpty()) return false;
        if (interruptMask.contains(InterruptMask.NONE)) return false;
        return interruptMask.contains(cause);
    }
}