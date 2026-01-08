package com.pgalaxyp.fragmento.rpg.engine;

import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.domain.weapon.WeaponDef;
import com.pgalaxyp.fragmento.rpg.core.port.TargetingResolved;
import com.pgalaxyp.fragmento.rpg.core.rule.ActionRule;
import com.pgalaxyp.fragmento.rpg.core.rule.ComboRule;
import com.pgalaxyp.fragmento.rpg.core.rule.RuleInput;
import com.pgalaxyp.fragmento.rpg.core.rule.RuleResult;
import com.pgalaxyp.fragmento.rpg.core.rule.TargetingResolution;
import com.pgalaxyp.fragmento.rpg.core.spec.CycleSpec;
import com.pgalaxyp.fragmento.rpg.core.state.ActionState;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.ComboState;
import com.pgalaxyp.fragmento.rpg.core.state.delta.DeltaBatch;
import com.pgalaxyp.fragmento.rpg.core.state.delta.StateDelta;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class RulePipeline {

    public record PassResult(
            List<DeltaBatch> batches,
            List<DomainEvent> events
    ) {}

    private final ActionRule actionRule;
    private final ComboRule comboRule;

    public RulePipeline(ActionRule actionRule, ComboRule comboRule) {
        this.actionRule = actionRule;
        this.comboRule = comboRule;
    }

    public PassResult runIntentPass(
            CycleSpec cycle,
            Map<Long, ActorState> baseActors,
            List<Intent> intents,
            WeaponCatalog weaponCatalog
    ) {
        if (intents == null || intents.isEmpty()) {
            return new PassResult(List.of(), List.of());
        }

        List<DeltaBatch> batches = new ArrayList<>();
        List<DomainEvent> events = new ArrayList<>();

        for (Intent intent : intents) {
            if (intent == null) {
                continue;
            }

            ActorState actor = baseActors == null ? null : baseActors.get(intent.actorId());
            ActionState currentAction = actor == null ? null : actor.actionState();

            WeaponDef weapon = null;
            if (weaponCatalog != null && intent.requestedAction() != null) {
                weapon = weaponCatalog.findByActionId(intent.requestedAction());
            }

            RuleResult result = actionRule.evaluate(
                    cycle,
                    intent.actorId(),
                    currentAction,
                    intent.requestedAction(),
                    weapon
            );

            collectResult(result, batches, events);
        }

        return new PassResult(
                Collections.unmodifiableList(batches),
                Collections.unmodifiableList(events)
        );
    }

    public PassResult runTargetingPass(
            CycleSpec cycle,
            Map<Long, ActorState> projectedActors,
            List<TargetingResolved> targetingResolvedBatch,
            WeaponCatalog weaponCatalog
    ) {
        boolean wantsTargeting = cycle != null
                && cycle.targeting() != null
                && cycle.targeting().targetingPerHit();

        List<DeltaBatch> batches = new ArrayList<>();
        List<DomainEvent> events = new ArrayList<>();

        if (projectedActors == null || projectedActors.isEmpty()) {
            return finalizeResult(batches, events);
        }

        if (!wantsTargeting) {
            List<Long> actorIds = new ArrayList<>(projectedActors.keySet());
            actorIds.sort(Long::compare);

            for (Long actorIdObj : actorIds) {
                if (actorIdObj == null) {
                    continue;
                }

                ActorState actor = projectedActors.get(actorIdObj);
                if (actor == null) {
                    continue;
                }

                ActionState action = actor.actionState();
                ComboState combo = actor.comboState();
                if (action == null || combo == null) {
                    continue;
                }

                WeaponDef weapon = null;
                if (weaponCatalog != null && action.actionId() != null) {
                    weapon = weaponCatalog.findByActionId(action.actionId());
                }

                RuleResult result = comboRule.evaluate(
                        cycle,
                        new RuleInput(actorIdObj, null),
                        action,
                        combo,
                        weapon
                );

                collectResult(result, batches, events);
            }

            return finalizeResult(batches, events);
        }

        if (targetingResolvedBatch == null || targetingResolvedBatch.isEmpty()) {
            return finalizeResult(batches, events);
        }

        for (TargetingResolved tr : targetingResolvedBatch) {
            if (tr == null) {
                continue;
            }

            ActorState actor = projectedActors.get(tr.actorId());
            if (actor == null) {
                continue;
            }

            ActionState action = actor.actionState();
            ComboState combo = actor.comboState();
            if (action == null || combo == null) {
                continue;
            }

            WeaponDef weapon = null;
            if (weaponCatalog != null && action.actionId() != null) {
                weapon = weaponCatalog.findByActionId(action.actionId());
            }

            TargetingResolution resolution = new TargetingResolution(tr.actorId(), tr.targetingId());

            RuleResult result = comboRule.evaluate(
                    cycle,
                    new RuleInput(tr.actorId(), resolution),
                    action,
                    combo,
                    weapon
            );

            collectResult(result, batches, events);
        }

        return finalizeResult(batches, events);
    }

    private static void collectResult(
            RuleResult result,
            List<DeltaBatch> batches,
            List<DomainEvent> events
    ) {
        if (result == null) {
            return;
        }

        List<StateDelta> deltas = result.deltas();
        if (deltas != null && !deltas.isEmpty()) {
            batches.add(new DeltaBatch(deltas));
        }

        List<DomainEvent> ev = result.events();
        if (ev != null && !ev.isEmpty()) {
            events.addAll(ev);
        }
    }

    private static PassResult finalizeResult(
            List<DeltaBatch> batches,
            List<DomainEvent> events
    ) {
        return new PassResult(
                Collections.unmodifiableList(batches),
                Collections.unmodifiableList(events)
        );
    }
}