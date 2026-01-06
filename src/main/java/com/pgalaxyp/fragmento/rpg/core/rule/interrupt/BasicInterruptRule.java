package com.pgalaxyp.fragmento.rpg.core.rule.interrupt;

import com.pgalaxyp.fragmento.rpg.core.domain.action.InterruptMask;
import com.pgalaxyp.fragmento.rpg.core.domain.event.ActionInterruptedEvent;
import com.pgalaxyp.fragmento.rpg.core.domain.event.RpgEvent;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import java.util.List;

public final class BasicInterruptRule implements InterruptRule {

    @Override
    public InterruptResult apply(long actorId, ActorState current, InterruptMask cause, long now) {
        if (cause == null || cause == InterruptMask.NONE) return InterruptResult.ignored();
        if (current == null) return InterruptResult.ignored();

        var currentAction = current.currentAction();
        if (currentAction == null) return InterruptResult.ignored();

        if (!currentAction.canBeInterruptedBy(cause)) return InterruptResult.ignored();

        var next = new ActorState(actorId, null, current.combo(), List.of());

        var events = List.<RpgEvent>of(new ActionInterruptedEvent(
                actorId,
                currentAction.actionId(),
                cause,
                now
        ));

        return new InterruptResult(next, events);
    }
}