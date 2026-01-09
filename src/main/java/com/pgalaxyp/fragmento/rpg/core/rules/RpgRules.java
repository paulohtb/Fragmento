package com.pgalaxyp.fragmento.rpg.core.rules;

import com.pgalaxyp.fragmento.rpg.core.content.RpgContent;
import com.pgalaxyp.fragmento.rpg.core.domain.def.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.def.EffectDef;
import com.pgalaxyp.fragmento.rpg.core.domain.def.WeaponDef;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.EffectId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.QueryId;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.HomingMagicSpec;
import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.event.delta.ComboAdvanced;
import com.pgalaxyp.fragmento.rpg.core.event.delta.ComboEnded;
import com.pgalaxyp.fragmento.rpg.core.event.delta.ComboStarted;
import com.pgalaxyp.fragmento.rpg.core.event.delta.DamageApplied;
import com.pgalaxyp.fragmento.rpg.core.event.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.event.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.event.event.HomingMagicVisualEvent;
import com.pgalaxyp.fragmento.rpg.core.event.intent.ComboAdvanceIntent;
import com.pgalaxyp.fragmento.rpg.core.event.intent.ComboStartIntent;
import com.pgalaxyp.fragmento.rpg.core.event.intent.DomainIntent;
import com.pgalaxyp.fragmento.rpg.core.event.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.rpg.core.event.query.ExternalQueryEvent;
import com.pgalaxyp.fragmento.rpg.core.event.query.TargetingQueryRequested;
import com.pgalaxyp.fragmento.rpg.core.event.resolution.DomainResolution;
import com.pgalaxyp.fragmento.rpg.core.event.resolution.TargetingCandidate;
import com.pgalaxyp.fragmento.rpg.core.event.resolution.TargetingQueryResolved;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.ComboState;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public final class RpgRules {

    public static RuleResult passA(
            FrameContext frame,
            GameState state,
            RpgContent content,
            List<IntentEnvelope> intents
    ) {
        if (frame == null || state == null || content == null || intents == null) {
            throw new IllegalArgumentException();
        }

        List<StateDelta> deltas = new ArrayList<>();
        List<DomainEvent> events = new ArrayList<>();
        List<ExternalQueryEvent> queries = new ArrayList<>();

        Map<ActorId, ComboState> comboByActor = new TreeMap<>();
        for (var e : state.actors().entrySet()) {
            ActorId actorId = e.getKey();
            ActorState actor = e.getValue();
            if (actor.combo().isPresent()) {
                comboByActor.put(actorId, actor.combo().get());
            }
        }

        int queryIndex = 0;

        for (IntentEnvelope env : intents) {
            if (env == null) {
                throw new IllegalArgumentException();
            }

            ActorId actorId = env.actorId();
            DomainIntent intent = env.intent();

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
                if (action.kind() == null) {
                    continue;
                }

                int stepsTotal = action.effectSequence().size();
                deltas.add(new ComboStarted(actorId, weapon.actionId(), weapon.id(), stepsTotal, frame.frameId()));

                QueryId qid = QueryId.fromFrame(frame.frameId(), queryIndex);
                queryIndex = Math.addExact(queryIndex, 1);
                queries.add(new TargetingQueryRequested(qid, actorId, weapon.actionId(), 0, action.cycle().targeting()));

                if (stepsTotal <= 1) {
                    deltas.add(new ComboEnded(actorId, weapon.actionId()));
                    continue;
                }

                comboByActor.put(actorId, new ComboState(weapon.actionId(), weapon.id(), 0, stepsTotal, frame.frameId()));
                continue;
            }

            if (intent instanceof ComboAdvanceIntent(ActionId actionId)) {
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

                QueryId qid = QueryId.fromFrame(frame.frameId(), queryIndex);
                queryIndex = Math.addExact(queryIndex, 1);
                queries.add(new TargetingQueryRequested(qid, actorId, combo.actionId(), nextStepIndex, action.cycle().targeting()));

                if (nextStepIndex >= lastStepIndex) {
                    deltas.add(new ComboEnded(actorId, combo.actionId()));
                    comboByActor.remove(actorId);
                } else {
                    comboByActor.put(actorId, new ComboState(combo.actionId(), combo.weaponId(), nextStepIndex, combo.stepsTotal(), frame.frameId()));
                }
            }
        }

        return new RuleResult(deltas, events, queries);
    }

    public static RuleResult passB(
            FrameContext frame,
            GameState state,
            RpgContent content,
            List<ExternalQueryEvent> queries,
            List<DomainResolution> resolutions
    ) {
        if (frame == null || state == null || content == null || queries == null || resolutions == null) {
            throw new IllegalArgumentException();
        }

        Map<QueryId, TargetingQueryResolved> targeting = new TreeMap<>();
        for (DomainResolution res : resolutions) {
            if (res == null) {
                throw new IllegalArgumentException();
            }
            if (res instanceof TargetingQueryResolved tr) {
                targeting.put(tr.queryId(), tr);
            }
        }

        List<StateDelta> deltas = new ArrayList<>();
        List<DomainEvent> events = new ArrayList<>();

        for (ExternalQueryEvent q : queries) {
            if (q == null) {
                throw new IllegalArgumentException();
            }
            if (!(q instanceof TargetingQueryRequested tr)) {
                continue;
            }

            TargetingQueryResolved resolved = targeting.get(tr.queryId());
            if (resolved == null) {
                resolved = TargetingQueryResolved.empty(tr.queryId());
            }

            Optional<ActorId> targetOpt = pickTarget(tr.sourceActorId(), resolved);
            if (targetOpt.isEmpty()) {
                continue;
            }

            Optional<ActionDef> actionOpt = content.findAction(tr.actionId());
            if (actionOpt.isEmpty()) {
                continue;
            }
            ActionDef action = actionOpt.get();

            int stepIndex = tr.stepIndex();
            if (stepIndex < 0 || stepIndex >= action.effectSequence().size()) {
                continue;
            }

            EffectId effectId = action.effectSequence().get(stepIndex);
            Optional<EffectDef> effectOpt = content.findEffect(effectId);
            if (effectOpt.isEmpty()) {
                continue;
            }
            EffectDef effect = effectOpt.get();

            ActorId targetId = targetOpt.get();
            deltas.add(new DamageApplied(targetId, effect.damage().hearts()));

            if (effect.visual().isPresent()) {
                if (effect.visual().get() instanceof HomingMagicSpec(int lifetimeFrames)) {
                    events.add(new HomingMagicVisualEvent(tr.sourceActorId(), targetId, lifetimeFrames));
                }
            }
        }

        return new RuleResult(deltas, events, List.of());
    }

    private static Optional<ActorId> pickTarget(ActorId sourceActorId, TargetingQueryResolved resolved) {
        if (sourceActorId == null || resolved == null) {
            return Optional.empty();
        }

        TargetingCandidate best = null;

        for (TargetingCandidate c : resolved.candidates()) {
            if (c == null || c.actorId() == null) {
                continue;
            }
            if (c.actorId().equals(sourceActorId)) {
                continue;
            }

            if (best == null) {
                best = c;
                continue;
            }

            int distCmp = Integer.compare(c.distanceSquared(), best.distanceSquared());
            if (distCmp < 0) {
                best = c;
                continue;
            }
            if (distCmp > 0) {
                continue;
            }

            int idCmp = c.actorId().compareTo(best.actorId());
            if (idCmp < 0) {
                best = c;
            }
        }

        if (best == null) {
            return Optional.empty();
        }
        return Optional.of(best.actorId());
    }

    private RpgRules() {}
}
