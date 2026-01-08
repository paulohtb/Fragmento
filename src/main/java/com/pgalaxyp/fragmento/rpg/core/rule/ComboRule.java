package com.pgalaxyp.fragmento.rpg.core.rule;

import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEventType;
import com.pgalaxyp.fragmento.rpg.core.domain.weapon.WeaponDef;
import com.pgalaxyp.fragmento.rpg.core.spec.CycleSpec;
import com.pgalaxyp.fragmento.rpg.core.state.ActionState;
import com.pgalaxyp.fragmento.rpg.core.state.ComboState;
import com.pgalaxyp.fragmento.rpg.core.state.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.delta.StateDeltaType;
import java.util.ArrayList;
import java.util.List;

public final class ComboRule {

    public RuleResult evaluate(
            CycleSpec cycle,
            RuleInput input,
            ActionState action,
            ComboState combo,
            WeaponDef weapon
    ) {
        if (input == null || action == null || combo == null || weapon == null) {
            return new RuleResult(List.of(), List.of());
        }

        if (weapon.combo() == null || weapon.combo().steps() == null) {
            return new RuleResult(List.of(), List.of());
        }

        int curIndex = combo.index();
        if (curIndex < 0 || curIndex >= weapon.combo().steps().size()) {
            return new RuleResult(List.of(), List.of());
        }

        var curStep = weapon.combo().steps().get(curIndex);
        if (curStep == null) {
            return new RuleResult(List.of(), List.of());
        }

        boolean wantsTargeting =
                cycle != null
                        && cycle.targeting() != null
                        && cycle.targeting().targetingPerHit();

        if (wantsTargeting) {
            TargetingResolution tr = input.targetingResolution();
            if (tr == null) {
                return new RuleResult(List.of(), List.of());
            }
            if (tr.actorId() != input.actorId()) {
                return new RuleResult(List.of(), List.of());
            }
            if (curStep.targetingId() != null) {
                if (tr.targetingId() == null) {
                    return new RuleResult(List.of(), List.of());
                }
                if (!curStep.targetingId().equals(tr.targetingId())) {
                    return new RuleResult(List.of(), List.of());
                }
            }
        }

        List<DomainEvent> events = new ArrayList<>();
        List<StateDelta> deltas = new ArrayList<>();

        if (curStep.effect() != null && curStep.effect().id() != null) {
            events.add(new DomainEvent(
                    DomainEventType.EFFECT_TRIGGERED,
                    input.actorId(),
                    curStep.effect().id(),
                    curIndex,
                    null
            ));
        }

        int nextIndex = curIndex + 1;

        if (nextIndex >= weapon.combo().steps().size()) {
            deltas.add(new StateDelta(
                    StateDeltaType.COMBO_CLEAR,
                    input.actorId(),
                    null,
                    null,
                    0
            ));
            deltas.add(new StateDelta(
                    StateDeltaType.ACTION_CLEAR,
                    input.actorId(),
                    null,
                    null,
                    0
            ));
            return new RuleResult(deltas, events);
        }

        var nextStep = weapon.combo().steps().get(nextIndex);
        if (nextStep == null) {
            return new RuleResult(deltas, events);
        }

        deltas.add(new StateDelta(
                StateDeltaType.COMBO_SET,
                input.actorId(),
                null,
                nextStep.stepId(),
                nextIndex
        ));

        if (wantsTargeting && nextStep.targetingId() != null) {
            events.add(new DomainEvent(
                    DomainEventType.TARGETING_REQUESTED,
                    input.actorId(),
                    null,
                    0,
                    nextStep.targetingId()
            ));
        }

        return new RuleResult(deltas, events);
    }
}