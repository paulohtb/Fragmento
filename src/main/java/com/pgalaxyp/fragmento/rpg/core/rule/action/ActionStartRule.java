package com.pgalaxyp.fragmento.rpg.core.rule.action;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.event.ActionStarted;
import com.pgalaxyp.fragmento.rpg.core.rule.RuleResult;
import com.pgalaxyp.fragmento.rpg.core.state.action.ActionState;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.delta.ActionStateDelta;
import java.util.List;

public final class ActionStartRule {

    public RuleResult declareStart(
            long actorId,
            ActorState current,
            ActionDef action,
            long startedAt
    ) {
        if (action == null) return RuleResult.empty();

        long duration = action.timeline().totalMillis();
        long endsAt = startedAt + duration;

        var next = new ActionState(
                action.id(),
                action.priority(),
                startedAt,
                endsAt,
                action.interruptMask()
        );

        var delta = new ActionStateDelta(actorId, next);
        var event = new ActionStarted(actorId, action.id(), startedAt, endsAt);

        return RuleResult.consumed(List.of(delta), List.of(event));
    }
}