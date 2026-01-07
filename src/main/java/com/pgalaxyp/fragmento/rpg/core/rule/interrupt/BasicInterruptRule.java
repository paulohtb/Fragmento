package com.pgalaxyp.fragmento.rpg.core.rule.interrupt;

import com.pgalaxyp.fragmento.rpg.core.domain.action.InterruptMask;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;

public final class BasicInterruptRule implements InterruptRule {

    @Override
    public ActorState apply(
            long actorId,
            ActorState current,
            InterruptMask cause,
            long now
    ) {
        if (current == null) return ActorState.empty(actorId);
        if (cause == null) return current;

        var action = current.currentAction();
        if (action == null) return current;
        if (!action.canBeInterruptedBy(cause)) return current;

        return current.with(null, current.combo());
    }
}