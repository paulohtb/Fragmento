package com.pgalaxyp.fragmento.rpg.core.rule;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionType;
import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.state.ActionState;
import com.pgalaxyp.fragmento.rpg.core.state.delta.ActionStateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.delta.ComboStateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.delta.StateDelta;
import java.util.List;

public final class ActionRule {

    public RuleFrame evaluate(
            long actorId,
            ActionState currentAction,
            ActionId requestedAction,
            ActionType actionType
    ) {
        if (currentAction != null) {
            return new RuleFrame(
                    new StateDelta(List.of(), List.of()),
                    List.of()
            );
        }

        ActionStateDelta actionDelta =
                new ActionStateDelta(actorId, requestedAction);

        ComboStateDelta comboInit =
                actionType == ActionType.COMBO
                        ? new ComboStateDelta(actorId, null, 0)
                        : null;

        StateDelta delta =
                new StateDelta(
                        List.of(actionDelta),
                        comboInit == null ? List.of() : List.of(comboInit)
                );

        DomainEvent event =
                new DomainEvent();

        return new RuleFrame(
                delta,
                List.of(event)
        );
    }
}