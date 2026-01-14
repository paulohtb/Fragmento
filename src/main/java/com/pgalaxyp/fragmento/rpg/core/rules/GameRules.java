package com.pgalaxyp.fragmento.rpg.core.rules;

import com.pgalaxyp.fragmento.rpg.core.time.*;
import com.pgalaxyp.fragmento.rpg.cycle.api.*;
import com.pgalaxyp.fragmento.rpg.combo.api.*;
import com.pgalaxyp.fragmento.rpg.action.api.*;
import com.pgalaxyp.fragmento.rpg.effect.api.*;
import com.pgalaxyp.fragmento.rpg.core.state.*;
import com.pgalaxyp.fragmento.rpg.action.emit.*;
import com.pgalaxyp.fragmento.rpg.core.content.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import com.pgalaxyp.fragmento.rpg.core.events.event.*;
import com.pgalaxyp.fragmento.rpg.core.events.delta.*;
import com.pgalaxyp.fragmento.rpg.core.events.intent.*;
import com.pgalaxyp.fragmento.rpg.effect.api.EffectService.*;
import java.util.*;

public final class GameRules {

    public static RuleResult pass(FrameContext frame, GameState state, GameContent content, List<IntentEnvelope> intents, ComboService combo, ActionCycleService cycles, ActionService actions, EffectService effects) {
        Objects.requireNonNull(frame, "frame cannot be null");
        Objects.requireNonNull(state, "state cannot be null");
        Objects.requireNonNull(content, "content cannot be null");
        Objects.requireNonNull(intents, "intents cannot be null");
        Objects.requireNonNull(combo, "combo cannot be null");
        Objects.requireNonNull(cycles, "cycles cannot be null");
        Objects.requireNonNull(actions, "actions cannot be null");
        Objects.requireNonNull(effects, "effects cannot be null");

        List<StateDelta> deltas = new ArrayList<>();
        List<DomainEvent> events = new ArrayList<>();

        int queryIndex = 0;
        int eventIndex = 0;

        for (IntentEnvelope env : intents) {
            if (env == null) { continue; }

            ActorId actorId = env.actorId();
            DomainIntent intent = env.intent();
            if (intent instanceof ActorJoinIntent) {
                if (state.findActor(actorId).isPresent()) { continue; }

                var clazz = content.classes().firstEntry().getValue();
                deltas.add(new ActorSpawned(actorId, clazz.id(), clazz.startingWeaponId(), 10, 10));
                continue;
            }

            if (!(intent instanceof PerformActionIntent p)) { continue; }

            Optional<ActorState> actorOpt = state.findActor(actorId);
            if (actorOpt.isEmpty()) { continue; }

            ActorState actor = actorOpt.get();
            if (actor.equippedWeaponId().isEmpty()) { continue; }

            WeaponId weaponId = actor.equippedWeaponId().get();
            ComboInput input = p.input();
            ComboResult comboResult = combo.decide(actorId, weaponId, input);
            if (comboResult instanceof ComboResult.Reset) {
                combo.reset(actorId);
                actions.clear(actorId);
                continue;
            }

            if (!(comboResult instanceof ComboResult.Progress progress)) { continue; }

            Optional<ActionRequest> reqOpt = cycles.translate(frame, actorId, weaponId, progress);
            if (reqOpt.isEmpty()) {
                combo.reset(actorId);
                actions.clear(actorId);
                continue;
            }

            ActionRequest req = reqOpt.get();
            ActionContext ctx = new ActionContext(actorId, weaponId, frame.frameId());
            ActionOutcome outcome = actions.handle(ctx, req);
            if (outcome instanceof ActionOutcome.Rejected) {
                combo.reset(actorId);
                actions.clear(actorId);
                continue;
            }
            if (outcome instanceof ActionOutcome.Ignored) { continue;}

            ActionOutcome.Accepted accepted = (ActionOutcome.Accepted) outcome;
            for (ActionEmission emission : accepted.emissions()) {
                if (!(emission instanceof EffectIntentEmission ee)) { continue; }

                EffectOutcome eo = effects.apply(frame, content, state, ctx, ee.intent(), queryIndex, eventIndex);

                deltas.addAll(eo.deltas());
                events.addAll(eo.events());

                queryIndex = Math.addExact(queryIndex, eo.queriesUsed());
                eventIndex = Math.addExact(eventIndex, eo.eventsUsed());
            }
        }

        return new RuleResult(deltas, events);
    }

    private GameRules() {}
}