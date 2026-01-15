package com.pgalaxyp.fragmento.combat.core.rules;

import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.cycle.api.*;
import com.pgalaxyp.fragmento.combat.action.api.*;
import com.pgalaxyp.fragmento.combat.effect.api.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.action.emit.*;
import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import com.pgalaxyp.fragmento.combat.core.events.event.*;
import com.pgalaxyp.fragmento.combat.core.events.delta.*;
import com.pgalaxyp.fragmento.combat.core.events.intent.*;
import java.util.*;

public final class GameRules {

    public static RuleResult pass(FrameContext frame, GameState state, List<IntentEnvelope> intents, ComboService combo, ActionCycleService cycles, ActionService actions, EffectService effects) {
        if (frame == null || state == null || intents == null || combo == null || cycles == null || actions == null || effects == null) throw new IllegalArgumentException();

        List<StateDelta> deltas = new ArrayList<>();
        List<DomainEvent> events = new ArrayList<>();
        for (IntentEnvelope env : intents) {
            if (env == null) continue;

            ActorId actorId = env.actorId();
            DomainIntent intent = env.intent();
            if (!(intent instanceof PerformActionIntent p)) continue;

            var actorOpt = state.findActor(actorId);
            if (actorOpt.isEmpty()) continue;

            var actor = actorOpt.get();
            if (actor.equippedWeaponId().isEmpty()) continue;

            WeaponId weaponId = actor.equippedWeaponId().get();
            ComboResult comboResult = combo.decide(actorId, weaponId, p.input());
            if (!(comboResult instanceof ComboResult.Progress progress)) continue;

            Optional<ActionRequest> reqOpt = cycles.translate(frame, actorId, weaponId, progress);
            if (reqOpt.isEmpty()) {
                combo.reset(actorId);
                actions.clear(actorId);
                continue;
            }

            ActionOutcome out = actions.handle(new ActionContext(actorId, weaponId, frame.frameId()), reqOpt.get());
            if (!(out instanceof ActionOutcome.Accepted a)) continue;

            for (ActionEmission e : a.emissions()) {
                if (e instanceof EffectIntentEmission ee) {
                    var eo = effects.apply(frame, state, new ActionContext(actorId, weaponId, frame.frameId()), ee.intent());
                    deltas.addAll(eo.deltas());
                    events.addAll(eo.events());
                }
            }
        }

        return new RuleResult(deltas, events);
    }

    private GameRules() {}
}