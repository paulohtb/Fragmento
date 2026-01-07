package com.pgalaxyp.fragmento.rpg.core.rule;

import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboStepId;
import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.domain.event.TargetingRequested;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingId;
import com.pgalaxyp.fragmento.rpg.core.state.delta.ComboStateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.delta.StateDelta;
import java.util.List;

public final class ComboRule {

    public RuleFrame evaluate(
            long actorId,
            ComboStepId nextStep,
            int nextIndex,
            TargetingId targetingId
    ) {
        ComboStateDelta comboDelta =
                new ComboStateDelta(actorId, nextStep, nextIndex);

        DomainEvent event =
                new TargetingRequested(actorId, targetingId);

        return new RuleFrame(
                new StateDelta(
                        List.of(),
                        List.of(comboDelta)
                ),
                List.of(event)
        );
    }
}