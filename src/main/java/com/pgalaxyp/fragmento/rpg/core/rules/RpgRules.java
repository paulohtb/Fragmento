package com.pgalaxyp.fragmento.rpg.core.rules;

import com.pgalaxyp.fragmento.rpg.action.command.*;
import com.pgalaxyp.fragmento.rpg.action.context.*;
import com.pgalaxyp.fragmento.rpg.action.emit.*;
import com.pgalaxyp.fragmento.rpg.action.key.*;
import com.pgalaxyp.fragmento.rpg.action.result.*;
import com.pgalaxyp.fragmento.rpg.action.runtime.*;
import com.pgalaxyp.fragmento.rpg.action.type.*;
import com.pgalaxyp.fragmento.rpg.core.content.*;
import com.pgalaxyp.fragmento.rpg.core.domain.def.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.*;
import com.pgalaxyp.fragmento.rpg.core.domain.time.*;
import com.pgalaxyp.fragmento.rpg.core.events.delta.*;
import com.pgalaxyp.fragmento.rpg.core.events.event.*;
import com.pgalaxyp.fragmento.rpg.core.events.intent.*;
import com.pgalaxyp.fragmento.rpg.core.state.*;
import com.pgalaxyp.fragmento.rpg.damage.api.*;
import com.pgalaxyp.fragmento.rpg.damage.domain.*;
import com.pgalaxyp.fragmento.rpg.damage.snapshot.*;
import com.pgalaxyp.fragmento.rpg.targeting.api.*;
import com.pgalaxyp.fragmento.rpg.targeting.bridge.*;
import com.pgalaxyp.fragmento.rpg.targeting.system.*;

import java.util.*;

public final class RpgRules {

    public static RuleResult pass(
            FrameContext frame,
            GameState state,
            RpgContent content,
            List<IntentEnvelope> intents,
            ActionRuntimeStore actions,
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

            if (!(intent instanceof PerformActionIntent p)) {
                continue;
            }

            var actorOpt = state.findActor(actorId);
            if (actorOpt.isEmpty()) {
                continue;
            }

            ActorState actor = actorOpt.get();
            if (actor.equippedWeaponId().isEmpty()) {
                continue;
            }

            WeaponId weaponId = actor.equippedWeaponId().get();
            WeaponDef weapon = content.weapon(weaponId);
            ActionKey actionKey = weapon.actionKey();
            ActionDefinition def = content.findAction(actionKey).orElse(null);
            if (def == null) {
                continue;
            }

            ActionContext ctx = new ActionContext(actorId, weaponId, frame.frameId());
            ActionCommand cmd = actions.hasActive(actorId)
                    ? new ActionAdvance(p.input())
                    : new ActionStart(actionKey);

            ActionResult result = actions.handle(ctx, def, cmd);
            if (result.status() != ActionStatus.ACCEPTED) {
                continue;
            }

            for (ActionEmission emission : result.emissions()) {
                if (emission instanceof EffectEmission ee) {
                    applyEffect(
                            frame,
                            state,
                            content,
                            targetingService,
                            world,
                            damageService,
                            damageSnapshots,
                            actorId,
                            ee.effectId(),
                            deltas,
                            events,
                            queryIndex++,
                            eventIndex++
                    );
                }
            }
        }

        return new RuleResult(deltas, events);
    }

    private static void applyEffect(
            FrameContext frame,
            GameState state,
            RpgContent content,
            TargetingService targetingService,
            WorldRaycastAccess world,
            DamageService damageService,
            DamageSnapshotProvider damageSnapshots,
            ActorId sourceActorId,
            EffectId effectId,
            List<StateDelta> deltas,
            List<DomainEvent> events,
            int queryIndex,
            int eventIndex
    ) {
        TargetingSpec targetingSpec = TargetingSpec.singleEnemy();

        TargetResult result = targetingService.resolve(
                new TargetingContext(sourceActorId, targetingSpec, world)
        );

        result.actorTargetOpt().ifPresent(targetId -> {
            EffectDef effect = content.effect(effectId);

            var snapshot = damageSnapshots.snapshot(state, sourceActorId, targetId);
            var damage = damageService.resolve(
                    new DamageRequest(sourceActorId, targetId, effect.damage()),
                    snapshot
            );

            deltas.add(new DamageApplied(targetId, damage.finalHearts()));

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