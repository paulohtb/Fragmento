package com.pgalaxyp.fragmento.rpg.core.rule;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.domain.event.TargetingRequested;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingId;
import com.pgalaxyp.fragmento.rpg.core.state.delta.ActionStateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.delta.ComboStateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.delta.StateDelta;
import java.util.List;

public final class ActionRule {

    public RuleFrame evaluate(
            long actorId,
            ActionDef actionDef,
            TargetingId targetingId
    ) {
        ActionStateDelta actionDelta =
                new ActionStateDelta(actorId, actionDef.id());

        ComboStateDelta comboDelta =
                new ComboStateDelta(actorId, null, 0);

        DomainEvent event =
                new TargetingRequested(actorId, targetingId);

        return new RuleFrame(
                new StateDelta(
                        List.of(actionDelta),
                        List.of(comboDelta)
                ),
                List.of(event)
        );
    }
}