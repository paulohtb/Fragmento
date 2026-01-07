package com.pgalaxyp.fragmento.rpg.core.domain.action;

import com.pgalaxyp.fragmento.rpg.core.domain.event.ActionStarted;
import com.pgalaxyp.fragmento.rpg.core.rule.RuleResult;
import com.pgalaxyp.fragmento.rpg.core.state.action.ActionState;
import com.pgalaxyp.fragmento.rpg.core.state.actor.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.delta.ActionStateDelta;
import java.util.List;
import java.util.Set;

public final class ActionStartRule {

    public RuleResult declareStart(
            long actorId,
            ActorState current,
            ActionDef action,
            long startedAt
    ) {
        if (action == null) return RuleResult.empty();

        long duration = Math.max(action.timeline().totalMillis(), 0L);
        long endsAt = startedAt + duration;

        var next = new ActionState(
                action.id(),
                action.priority(),
                startedAt,
                endsAt,
                Set.of()
        );

        var delta = new ActionStateDelta(actorId, next);
        var event = new ActionStarted(actorId, action.id(), startedAt, endsAt);

        return RuleResult.of(List.of(delta), List.of(event));
    }
}