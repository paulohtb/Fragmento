package com.pgalaxyp.fragmento.rpg.core.rule;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionType;
import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboStepDef;
import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEventType;
import com.pgalaxyp.fragmento.rpg.core.domain.weapon.WeaponDef;
import com.pgalaxyp.fragmento.rpg.core.spec.CycleSpec;
import com.pgalaxyp.fragmento.rpg.core.state.ActionState;
import com.pgalaxyp.fragmento.rpg.core.state.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.delta.StateDeltaType;
import java.util.List;

public final class ActionRule {

    public RuleResult evaluate(
            CycleSpec cycle,
            long actorId,
            ActionState currentAction,
            ActionId requestedAction,
            WeaponDef weapon
    ) {
        if (currentAction != null) {
            return new RuleResult(List.of(), List.of());
        }

        if (requestedAction == null || weapon == null) {
            return new RuleResult(List.of(), List.of());
        }

        if (weapon.action() == null || weapon.action().type() == null) {
            return new RuleResult(List.of(), List.of());
        }

        StateDelta actionDelta = new StateDelta(
                StateDeltaType.ACTION_SET,
                actorId,
                requestedAction,
                null,
                0
        );

        if (weapon.action().type() != ActionType.COMBO) {
            return new RuleResult(List.of(actionDelta), List.of());
        }

        if (weapon.combo() == null || weapon.combo().steps() == null || weapon.combo().steps().isEmpty()) {
            return new RuleResult(List.of(actionDelta), List.of());
        }

        ComboStepDef firstStep = weapon.combo().steps().get(0);
        if (firstStep == null) {
            return new RuleResult(List.of(actionDelta), List.of());
        }

        StateDelta comboInit = new StateDelta(
                StateDeltaType.COMBO_SET,
                actorId,
                null,
                firstStep.stepId(),
                0
        );

        List<DomainEvent> events = initialEvents(cycle, actorId, firstStep);

        return new RuleResult(
                List.of(actionDelta, comboInit),
                events
        );
    }

    private static List<DomainEvent> initialEvents(CycleSpec cycle, long actorId, ComboStepDef firstStep) {
        boolean wantsTargeting =
                cycle != null
                        && cycle.targeting() != null
                        && cycle.targeting().targetingPerHit();

        if (!wantsTargeting) {
            return List.of();
        }

        if (firstStep.targetingId() == null) {
            return List.of();
        }

        return List.of(new DomainEvent(
                DomainEventType.TARGETING_REQUESTED,
                actorId,
                null,
                0,
                firstStep.targetingId()
        ));
    }
}