package com.pgalaxyp.fragmento.rpg.core.rules;

import com.pgalaxyp.fragmento.rpg.core.content.RpgContent;
import com.pgalaxyp.fragmento.rpg.core.domain.def.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.def.EffectDef;
import com.pgalaxyp.fragmento.rpg.core.domain.def.WeaponDef;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.EffectId;
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
import com.pgalaxyp.fragmento.rpg.core.event.query.QueryId;
import com.pgalaxyp.fragmento.rpg.core.event.query.TargetingQueryRequested;
import com.pgalaxyp.fragmento.rpg.core.event.resolution.DomainResolution;
import com.pgalaxyp.fragmento.rpg.core.event.resolution.TargetingQueryResolved;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.ComboState;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        int queryIndex = 0;

        for (IntentEnvelope env : intents) {
            if (env == null) {
                throw new IllegalArgumentException();
            }

            ActorId actorId = env.actorId();
            DomainIntent intent = env.intent();

            if (intent instanceof ComboStartIntent) {
                Optional<ActorState> actorOpt = state.findActor(actorId);
                if (actorOpt.isEmpty()) {
                    continue;
                }
                ActorState actor = actorOpt.get();
                if (actor.combo().isPresent()) {
                    continue;
                }
                if (actor.equippedWeaponId().isEmpty()) {
                    continue;
                }
                WeaponDef weapon = content.weapon(actor.equippedWeaponId().get());
                ActionDef action = content.action(weapon.actionId());
                int stepsTotal = action.cycle().combo().maxStepsTotal();

                deltas.add(new ComboStarted(actorId, weapon.actionId(), weapon.id(), stepsTotal));

                QueryId qid = QueryId.fromFrame(frame.frameId(), queryIndex);
                queryIndex += 1;
                queries.add(new TargetingQueryRequested(qid, actorId, action.cycle().targeting()));
                continue;
            }

            if (intent instanceof ComboAdvanceIntent advance) {
                ActionId actionId = advance.actionId();

                Optional<ActorState> actorOpt = state.findActor(actorId);
                if (actorOpt.isEmpty()) {
                    continue;
                }
                ActorState actor = actorOpt.get();
                if (actor.combo().isEmpty()) {
                    continue;
                }
                ComboState combo = actor.combo().get();
                if (!combo.actionId().equals(actionId)) {
                    continue;
                }

                int nextStepIndex = combo.stepIndex() + 1;
                if (nextStepIndex >= combo.stepsTotal()) {
                    deltas.add(new ComboEnded(actorId, combo.actionId()));
                    continue;
                }

                deltas.add(new ComboAdvanced(actorId));

                ActionDef action = content.action(combo.actionId());
                QueryId qid = QueryId.fromFrame(frame.frameId(), queryIndex);
                queryIndex += 1;
                queries.add(new TargetingQueryRequested(qid, actorId, action.cycle().targeting()));
            }
        }

        return new RuleResult(deltas, events, queries);
    }

    public static RuleResult passB(
            FrameContext frame,
            GameState state,
            RpgContent content,
            List<IntentEnvelope> intents,
            List<DomainResolution> resolutions
    ) {
        if (frame == null || state == null || content == null || intents == null || resolutions == null) {
            throw new IllegalArgumentException();
        }

        Map<QueryId, TargetingQueryResolved> targeting = new LinkedHashMap<>();
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
        List<ExternalQueryEvent> queries = List.of();

        int queryIndex = 0;

        for (IntentEnvelope env : intents) {
            if (env == null) {
                throw new IllegalArgumentException();
            }

            ActorId actorId = env.actorId();
            DomainIntent intent = env.intent();

            if (intent instanceof ComboStartIntent) {
                Optional<ActorState> actorOpt = state.findActor(actorId);
                if (actorOpt.isEmpty()) {
                    continue;
                }
                ActorState actor = actorOpt.get();
                if (actor.combo().isPresent()) {
                    continue;
                }
                if (actor.equippedWeaponId().isEmpty()) {
                    continue;
                }
                WeaponDef weapon = content.weapon(actor.equippedWeaponId().get());
                ActionDef action = content.action(weapon.actionId());

                QueryId qid = QueryId.fromFrame(frame.frameId(), queryIndex);
                queryIndex += 1;

                TargetingQueryResolved resolved = targeting.get(qid);
                if (resolved == null || resolved.targetActorId().isEmpty()) {
                    continue;
                }

                EffectId effectId = action.effectSequence().getFirst();
                EffectDef effect = content.effect(effectId);

                ActorId targetId = resolved.targetActorId().get();
                deltas.add(new DamageApplied(targetId, effect.damage().hearts()));
                events.add(new HomingMagicVisualEvent(actorId, targetId));
                continue;
            }

            if (intent instanceof ComboAdvanceIntent advance) {
                ActionId actionId = advance.actionId();

                Optional<ActorState> actorOpt = state.findActor(actorId);
                if (actorOpt.isEmpty()) {
                    continue;
                }
                ActorState actor = actorOpt.get();
                if (actor.combo().isEmpty()) {
                    continue;
                }
                ComboState combo = actor.combo().get();
                if (!combo.actionId().equals(actionId)) {
                    continue;
                }

                int nextStepIndex = combo.stepIndex() + 1;
                if (nextStepIndex >= combo.stepsTotal()) {
                    continue;
                }

                ActionDef action = content.action(combo.actionId());

                QueryId qid = QueryId.fromFrame(frame.frameId(), queryIndex);
                queryIndex += 1;

                TargetingQueryResolved resolved = targeting.get(qid);
                if (resolved == null || resolved.targetActorId().isEmpty()) {
                    continue;
                }

                if (nextStepIndex >= action.effectSequence().size()) {
                    continue;
                }

                EffectId effectId = action.effectSequence().get(nextStepIndex);
                EffectDef effect = content.effect(effectId);

                ActorId targetId = resolved.targetActorId().get();
                deltas.add(new DamageApplied(targetId, effect.damage().hearts()));
                events.add(new HomingMagicVisualEvent(actorId, targetId));
            }
        }

        return new RuleResult(deltas, events, queries);
    }

    private RpgRules() {}
}