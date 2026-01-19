package com.pgalaxyp.fragmento.combat.core.rules;

import com.pgalaxyp.fragmento.combat.action.api.*;
import com.pgalaxyp.fragmento.combat.action.emit.*;
import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.combo.skill.*;
import com.pgalaxyp.fragmento.combat.combo.state.*;
import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.cycle.api.*;
import com.pgalaxyp.fragmento.combat.delta.*;
import com.pgalaxyp.fragmento.combat.effect.api.*;
import com.pgalaxyp.fragmento.combat.effect.model.*;
import com.pgalaxyp.fragmento.combat.event.*;
import com.pgalaxyp.fragmento.combat.intent.*;
import java.util.*;

public final class GameRules {

    public static RuleResult pass(
            FrameContext frame,
            GameState state,
            List<IntentEnvelope> intents,
            GameContent content,
            ComboTracker comboTracker,
            ComboSkillResolver comboSkills,
            ComboService combo,
            ActionCycleService cycles,
            ActionService actions,
            EffectService effects
    ) {
        List<StateDelta> deltas = new ArrayList<>();
        List<DomainEvent> events = new ArrayList<>();
        SpawnDefaults defaults = content.defaults();
        Set<ActorId> actionTouchedThisFrame = new HashSet<>();

        for (IntentEnvelope env : intents) {
            ActorId actorId = env.actorId();

            if (env.intent() instanceof ActorJoinIntent) {
                if (state.findActor(actorId).isEmpty()) {
                    deltas.add(new ActorSpawned(actorId, defaults.classId(), defaults.startingWeaponId(), defaults.healthHearts(), defaults.maxHealthHearts()));
                }
                continue;
            }

            if (!(env.intent() instanceof PerformActionIntent p)) continue;

            ActorState actor = state.findActor(actorId).orElse(null);
            if (actor == null) continue;

            WeaponId weaponId = actor.equippedWeaponId().orElse(null);
            if (weaponId == null) continue;

            var entry = content.combos().baseFor(weaponId).orElse(null);
            if (entry == null) continue;

            ComboId comboId = entry.comboId();
            ComboPattern pattern = comboSkills.resolve(actorId, weaponId, entry.pattern());
            Optional<ComboState> prev = comboTracker.get(actorId);

            ComboResult result = combo.decide(comboId, pattern, p.input(), prev);

            if (result instanceof ComboResult.Reset) {
                comboTracker.clear(actorId);
                continue;
            }

            if (!(result instanceof ComboResult.Progress progress)) continue;

            if (progress.end()) comboTracker.clear(actorId);
            else comboTracker.put(actorId, new ComboState(progress.comboId(), progress.stepIndex(), progress.stepsTotal()));

            Optional<ActionRequest> reqOpt = cycles.translate(progress, actorId, weaponId, frame.frameId());
            if (reqOpt.isEmpty()) continue;

            ActionOutcome out = actions.handle(actorId, weaponId, frame.frameId(), reqOpt.get());
            actionTouchedThisFrame.add(actorId);

            if (out instanceof ActionOutcome.Success s) {
                var eo = effects.applyAll(frame, state, toEffectIntents(s.emissions()), actorId);
                deltas.addAll(eo.deltas());
                events.addAll(eo.events());
            }
        }

        for (var e : state.actors().entrySet()) {
            ActorId actorId = e.getKey();
            if (actionTouchedThisFrame.contains(actorId)) continue;
            if (!actions.hasActive(actorId)) continue;

            WeaponId weaponId = e.getValue().equippedWeaponId().orElse(null);
            if (weaponId == null) continue;

            ActionOutcome out = actions.handle(actorId, weaponId, frame.frameId(), ActionRequest.Tick.INSTANCE);
            if (out instanceof ActionOutcome.Success s) {
                var eo = effects.applyAll(frame, state, toEffectIntents(s.emissions()), actorId);
                deltas.addAll(eo.deltas());
                events.addAll(eo.events());
            }
        }

        return new RuleResult(deltas, events);
    }

    private static List<EffectIntent> toEffectIntents(List<ActionEmission> emissions) {
        if (emissions.isEmpty()) return List.of();
        ArrayList<EffectIntent> out = new ArrayList<>(emissions.size());
        for (var e : emissions) if (e instanceof EffectIntentEmission ei) out.add(ei.intent());
        return out.isEmpty() ? List.of() : List.copyOf(out);
    }

    private GameRules() {}
}