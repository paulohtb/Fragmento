package com.pgalaxyp.fragmento.rpg.core.rules;

import com.pgalaxyp.fragmento.rpg.core.content.RpgContent;
import com.pgalaxyp.fragmento.rpg.core.domain.def.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.def.ClassDef;
import com.pgalaxyp.fragmento.rpg.core.domain.def.EffectDef;
import com.pgalaxyp.fragmento.rpg.core.domain.def.WeaponDef;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.EffectId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.QueryId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.HomingMagicSpec;
import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.events.delta.ActorSpawned;
import com.pgalaxyp.fragmento.rpg.core.events.delta.ComboAdvanced;
import com.pgalaxyp.fragmento.rpg.core.events.delta.ComboEnded;
import com.pgalaxyp.fragmento.rpg.core.events.delta.ComboStarted;
import com.pgalaxyp.fragmento.rpg.core.events.delta.DamageApplied;
import com.pgalaxyp.fragmento.rpg.core.events.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.events.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.events.event.HomingMagicVisualEvent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.ActorJoinIntent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.ComboAdvanceIntent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.ComboStartIntent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.DomainIntent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.ComboState;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import com.pgalaxyp.fragmento.rpg.targeting.api.ActorTarget;
import com.pgalaxyp.fragmento.rpg.targeting.api.Target;
import com.pgalaxyp.fragmento.rpg.targeting.api.TargetResult;
import com.pgalaxyp.fragmento.rpg.targeting.api.TargetingService;
import com.pgalaxyp.fragmento.rpg.targeting.api.TargetingSpec;
import com.pgalaxyp.fragmento.rpg.targeting.bridge.WorldRaycastAccess;
import com.pgalaxyp.fragmento.rpg.targeting.system.TargetingContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public final class RpgRules {

    public static RuleResult pass(
            FrameContext frame,
            GameState state,
            RpgContent content,
            List<IntentEnvelope> intents,
            TargetingService targetingService,
            WorldRaycastAccess world
    ) {
        if (frame == null || state == null || content == null || intents == null || targetingService == null || world == null) {
            throw new IllegalArgumentException();
        }

        List<StateDelta> deltas = new ArrayList<>();
        List<DomainEvent> events = new ArrayList<>();

        Map<ActorId, ComboState> comboByActor = new TreeMap<>();
        for (var e : state.actors().entrySet()) {
            ActorId actorId = e.getKey();
            ActorState actor = e.getValue();
            if (actor.combo().isPresent()) {
                comboByActor.put(actorId, actor.combo().get());
            }
        }

        int queryIndex = 0;
        int eventIndex = 0;

        for (IntentEnvelope env : intents) {
            if (env == null) {
                throw new IllegalArgumentException();
            }

            ActorId actorId = env.actorId();
            DomainIntent intent = env.intent();

            if (intent instanceof ActorJoinIntent) {
                if (state.findActor(actorId).isPresent()) {
                    continue;
                }
                var first = content.classes().firstEntry();
                if (first == null) {
                    continue;
                }
                ClassDef clazz = first.getValue();
                WeaponId weaponId = clazz.startingWeaponId();
                deltas.add(new ActorSpawned(actorId, clazz.id(), weaponId, 10, 10));
                continue;
            }

            if (intent instanceof ComboStartIntent) {
                if (comboByActor.containsKey(actorId)) {
                    continue;
                }

                Optional<ActorState> actorOpt = state.findActor(actorId);
                if (actorOpt.isEmpty()) {
                    continue;
                }
                ActorState actor = actorOpt.get();
                if (actor.equippedWeaponId().isEmpty()) {
                    continue;
                }

                Optional<WeaponDef> weaponOpt = content.findWeapon(actor.equippedWeaponId().get());
                if (weaponOpt.isEmpty()) {
                    continue;
                }
                WeaponDef weapon = weaponOpt.get();

                Optional<ActionDef> actionOpt = content.findAction(weapon.actionId());
                if (actionOpt.isEmpty()) {
                    continue;
                }
                ActionDef action = actionOpt.get();

                int stepsTotal = action.effectSequence().size();
                deltas.add(new ComboStarted(actorId, weapon.actionId(), weapon.id(), stepsTotal, frame.frameId()));

                var applied = applyStep(
                        frame,
                        state,
                        content,
                        targetingService,
                        world,
                        actorId,
                        action,
                        0,
                        deltas,
                        events,
                        queryIndex,
                        eventIndex
                );
                queryIndex = applied.queryIndex;
                eventIndex = applied.eventIndex;

                if (stepsTotal <= 1) {
                    deltas.add(new ComboEnded(actorId, weapon.actionId()));
                    continue;
                }

                comboByActor.put(actorId, new ComboState(weapon.actionId(), weapon.id(), 0, stepsTotal, frame.frameId()));
                continue;
            }

            if (intent instanceof ComboAdvanceIntent ca) {
                ActionId actionId = ca.actionId();

                ComboState combo = comboByActor.get(actorId);
                if (combo == null) {
                    continue;
                }
                if (!combo.actionId().equals(actionId)) {
                    continue;
                }

                int stepsTotal = combo.stepsTotal();
                int lastStepIndex = Math.subtractExact(stepsTotal, 1);

                if (combo.stepIndex() >= lastStepIndex) {
                    deltas.add(new ComboEnded(actorId, combo.actionId()));
                    comboByActor.remove(actorId);
                    continue;
                }

                Optional<ActionDef> actionOpt = content.findAction(combo.actionId());
                if (actionOpt.isEmpty()) {
                    continue;
                }
                ActionDef action = actionOpt.get();

                long diffFrames = Math.subtractExact(frame.frameId(), combo.lastStepFrameId());
                long framesPerStep = action.cycle().stepWindow().framesPerStep();
                if (diffFrames < framesPerStep) {
                    continue;
                }

                int nextStepIndex = Math.addExact(combo.stepIndex(), 1);
                if (nextStepIndex > lastStepIndex) {
                    deltas.add(new ComboEnded(actorId, combo.actionId()));
                    comboByActor.remove(actorId);
                    continue;
                }

                deltas.add(new ComboAdvanced(actorId, frame.frameId()));

                var applied = applyStep(
                        frame,
                        state,
                        content,
                        targetingService,
                        world,
                        actorId,
                        action,
                        nextStepIndex,
                        deltas,
                        events,
                        queryIndex,
                        eventIndex
                );
                queryIndex = applied.queryIndex;
                eventIndex = applied.eventIndex;

                if (nextStepIndex >= lastStepIndex) {
                    deltas.add(new ComboEnded(actorId, combo.actionId()));
                    comboByActor.remove(actorId);
                } else {
                    comboByActor.put(
                            actorId,
                            new ComboState(combo.actionId(), combo.weaponId(), nextStepIndex, combo.stepsTotal(), frame.frameId())
                    );
                }
            }
        }

        return new RuleResult(deltas, events);
    }

    private static ApplyResult applyStep(
            FrameContext frame,
            GameState state,
            RpgContent content,
            TargetingService targetingService,
            WorldRaycastAccess world,
            ActorId sourceActorId,
            ActionDef action,
            int stepIndex,
            List<StateDelta> deltas,
            List<DomainEvent> events,
            int queryIndex,
            int eventIndex
    ) {
        if (frame == null || state == null || content == null || targetingService == null || world == null || sourceActorId == null || action == null) {
            return new ApplyResult(queryIndex, eventIndex);
        }
        if (stepIndex < 0 || stepIndex >= action.effectSequence().size()) {
            return new ApplyResult(queryIndex, eventIndex);
        }

        Optional<ActorId> targetOpt = resolveTargetActorId(targetingService, world, sourceActorId, action.cycle().targeting());
        if (targetOpt.isEmpty()) {
            return new ApplyResult(queryIndex, eventIndex);
        }
        ActorId targetId = targetOpt.get();

        EffectId effectId = action.effectSequence().get(stepIndex);
        Optional<EffectDef> effectOpt = content.findEffect(effectId);
        if (effectOpt.isEmpty()) {
            return new ApplyResult(queryIndex, eventIndex);
        }
        EffectDef effect = effectOpt.get();

        deltas.add(new DamageApplied(targetId, effect.damage().hearts()));

        if (effect.visual().isPresent() && effect.visual().get() instanceof HomingMagicSpec hm) {
            QueryId qid = QueryId.fromFrame(frame.frameId(), queryIndex);
            queryIndex = Math.addExact(queryIndex, 1);

            int localIndex = eventIndex;
            eventIndex = Math.addExact(eventIndex, 1);

            events.add(new HomingMagicVisualEvent(
                    frame.frameId(),
                    localIndex,
                    qid,
                    sourceActorId,
                    targetId,
                    hm.lifetimeFrames()
            ));
        }

        return new ApplyResult(queryIndex, eventIndex);
    }

    private static Optional<ActorId> resolveTargetActorId(
            TargetingService targetingService,
            WorldRaycastAccess world,
            ActorId sourceActorId,
            TargetingSpec spec
    ) {
        if (targetingService == null || world == null || sourceActorId == null || spec == null) {
            return Optional.empty();
        }

        TargetResult res = targetingService.resolve(new TargetingContext(sourceActorId, spec, world));
        if (res == null) {
            return Optional.empty();
        }

        Target t = res.target();
        if (t instanceof ActorTarget at) {
            ActorId targetId = at.actorId();
            if (targetId.equals(sourceActorId)) {
                return Optional.empty();
            }
            return Optional.of(targetId);
        }

        return Optional.empty();
    }

    private record ApplyResult(int queryIndex, int eventIndex) {}

    private RpgRules() {}
}