package com.pgalaxyp.fragmento.rpg.core.rules;

import com.pgalaxyp.fragmento.rpg.core.content.RpgContent;
import com.pgalaxyp.fragmento.rpg.core.domain.def.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.def.EffectDef;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.EffectId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.QueryId;
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
import com.pgalaxyp.fragmento.rpg.core.events.intent.DomainIntent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.rpg.core.events.intent.PerformActionIntent;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import com.pgalaxyp.fragmento.rpg.damage.api.DamageService;
import com.pgalaxyp.fragmento.rpg.damage.domain.DamageRequest;
import com.pgalaxyp.fragmento.rpg.damage.snapshot.DamageSnapshotProvider;
import com.pgalaxyp.fragmento.rpg.targeting.api.TargetResult;
import com.pgalaxyp.fragmento.rpg.targeting.api.TargetingService;
import com.pgalaxyp.fragmento.rpg.targeting.bridge.WorldRaycastAccess;
import com.pgalaxyp.fragmento.rpg.targeting.system.TargetingContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class RpgRules {

    private static final ActionResolver ACTION_RESOLVER = new ActionResolver();

    public static RuleResult pass(
            FrameContext frame,
            GameState state,
            RpgContent content,
            List<IntentEnvelope> intents,
            TargetingService targetingService,
            WorldRaycastAccess world,
            DamageService damageService,
            DamageSnapshotProvider damageSnapshots
    ) {
        List<StateDelta> deltas = new ArrayList<>();
        List<DomainEvent> events = new ArrayList<>();

        int queryIndex = 0;
        int eventIndex = 0;

        for (var env : intents) {
            ActorId actorId = env.actorId();
            DomainIntent intent = env.intent();

            if (intent instanceof ActorJoinIntent) {
                if (state.findActor(actorId).isPresent()) {
                    continue;
                }
                var clazz = content.classes().firstEntry().getValue();
                deltas.add(new ActorSpawned(actorId, clazz.id(), clazz.startingWeaponId(), 10, 10));
                continue;
            }

            if (intent instanceof PerformActionIntent p) {
                var actorOpt = state.findActor(actorId);
                if (actorOpt.isEmpty()) {
                    continue;
                }

                ActorState actor = actorOpt.get();
                Optional<ActionId> actionIdOpt = ACTION_RESOLVER.resolvePrimaryAction(actor, content);
                if (actionIdOpt.isEmpty()) {
                    continue;
                }

                ActionDef action = content.findAction(actionIdOpt.get()).orElse(null);
                if (action == null) {
                    continue;
                }

                int step = p.stepIndex();
                if (step < 0 || step >= action.effectSequence().size()) {
                    continue;
                }

                boolean starting = actor.combo().isEmpty();
                if (starting && step != 0) {
                    continue;
                }

                if (!starting) {
                    var combo = actor.combo().get();
                    if (!combo.actionId().equals(action.id()) || combo.stepIndex() + 1 != step) {
                        continue;
                    }
                }

                applyStep(
                        frame,
                        state,
                        content,
                        targetingService,
                        world,
                        damageService,
                        damageSnapshots,
                        actorId,
                        action,
                        step,
                        deltas,
                        events,
                        queryIndex++,
                        eventIndex++
                );

                int stepsTotal = action.effectSequence().size();

                if (starting) {
                    deltas.add(new ComboStarted(actorId, action.id(), actor.equippedWeaponId().orElseThrow(), stepsTotal, frame.frameId()));
                } else {
                    deltas.add(new ComboAdvanced(actorId, frame.frameId()));
                }

                if (step == stepsTotal - 1) {
                    deltas.add(new ComboEnded(actorId, action.id()));
                }
            }
        }

        return new RuleResult(deltas, events);
    }

    private static void applyStep(
            FrameContext frame,
            GameState state,
            RpgContent content,
            TargetingService targetingService,
            WorldRaycastAccess world,
            DamageService damageService,
            DamageSnapshotProvider damageSnapshots,
            ActorId sourceActorId,
            ActionDef action,
            int stepIndex,
            List<StateDelta> deltas,
            List<DomainEvent> events,
            int queryIndex,
            int eventIndex
    ) {
        TargetResult res = targetingService.resolve(
                new TargetingContext(sourceActorId, action.cycle().targeting(), world)
        );

        res.actorTargetOpt().ifPresent(targetId -> {
            EffectId effectId = action.effectSequence().get(stepIndex);
            EffectDef effect = content.effect(effectId);

            var snap = damageSnapshots.snapshot(state, sourceActorId, targetId);
            var dmg = damageService.resolve(new DamageRequest(sourceActorId, targetId, effect.damage()), snap);

            deltas.add(new DamageApplied(targetId, dmg.finalHearts()));

            effect.visual().ifPresent(v -> {
                if (v instanceof HomingMagicSpec hm) {
                    QueryId qid = QueryId.fromFrame(frame.frameId(), queryIndex);
                    events.add(new HomingMagicVisualEvent(
                            frame.frameId(),
                            eventIndex,
                            qid,
                            sourceActorId,
                            targetId,
                            hm.lifetimeFrames()
                    ));
                }
            });
        });
    }

    private RpgRules() {}
}