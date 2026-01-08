package com.pgalaxyp.fragmento.rpg.core.rule;

import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboStepId;
import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.state.ComboState;
import com.pgalaxyp.fragmento.rpg.core.state.delta.ComboStateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.delta.StateDelta;
import java.util.List;

public final class ComboRule {

    public RuleFrame evaluate(
            long actorId,
            ComboState currentCombo,
            List<ComboStepId> sequence
    ) {
        int nextIndex =
                currentCombo == null ? 0 : currentCombo.index() + 1;

        if (nextIndex >= sequence.size()) {
            return new RuleFrame(
                    new StateDelta(List.of(), List.of()),
                    List.of()
            );
        }

        ComboStepId nextStep = sequence.get(nextIndex);

        ComboStateDelta delta =
                new ComboStateDelta(actorId, nextStep, nextIndex);

        DomainEvent event =
                new DomainEvent();

        return new RuleFrame(
                new StateDelta(List.of(), List.of(delta)),
                List.of(event)
        );
    }
}