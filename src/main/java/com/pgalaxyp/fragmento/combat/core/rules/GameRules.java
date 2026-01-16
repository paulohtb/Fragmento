package com.pgalaxyp.fragmento.combat.core.rules;

import com.pgalaxyp.fragmento.combat.action.api.*;
import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.combo.skill.*;
import com.pgalaxyp.fragmento.combat.combo.state.*;
import com.pgalaxyp.fragmento.combat.content.defaults.DefaultIds;
import com.pgalaxyp.fragmento.combat.content.catalog.*;
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
            ComboCatalog combos,
            ComboTracker comboTracker,
            ComboSkillResolver comboSkills,
            ComboService combo,
            ActionCycleService cycles,
            ActionService actions,
            EffectService effects
    ) {

        List<StateDelta> deltas = new ArrayList<>();
        List<DomainEvent> events = new ArrayList<>();

        for (IntentEnvelope env : intents) {

            ActorId actorId = env.actorId();

            if (env.intent() instanceof ActorJoinIntent) {
                if (state.findActor(actorId).isEmpty()) {
                    deltas.add(new ActorSpawned(
                            actorId,
                            DefaultIds.CLASS_DEFAULT,
                            DefaultIds.WEAPON_FLUTE,
                            20,
                            20
                    ));
                }
                continue;
            }

            if (!(env.intent() instanceof PerformActionIntent p)) continue;

            ActorState actor = state.findActor(actorId).orElse(null);
            if (actor == null || actor.equippedWeaponId().isEmpty()) continue;

            WeaponId weaponId = actor.equippedWeaponId().get();
            var entry = combos.baseFor(weaponId).orElse(null);
            if (entry == null) continue;

            ComboPattern pattern = comboSkills.resolve(actorId, weaponId, entry.pattern());
            Optional<ComboState> prev = comboTracker.get(actorId);

            ComboResult result = combo.decide(entry.comboId(), pattern, p.input(), prev);
            if (!(result instanceof ComboResult.Progress progress)) continue;

            Optional<ComboState> next =
                    progress.end()
                            ? Optional.empty()
                            : combo.advanceState(prev.orElseGet(() -> combo.start(entry.comboId(), pattern)), progress.stepsTotal());

            next.ifPresentOrElse(
                    s -> comboTracker.put(actorId, s),
                    () -> comboTracker.clear(actorId)
            );

            Optional<ActionRequest> reqOpt = cycles.translate(progress, actorId, weaponId, frame.frameId());
            if (reqOpt.isEmpty()) continue;

            ActionOutcome out = actions.handle(actorId, weaponId, frame.frameId(), reqOpt.get());
            if (!(out instanceof ActionOutcome.Success s)) continue;

            for (EffectIntent intent : s.intents()) {
                EffectOutcome eo = effects.apply(frame, state, intent, actorId);
                deltas.addAll(eo.deltas());
                events.addAll(eo.events());
            }
        }

        return new RuleResult(deltas, events);
    }

    private GameRules() {}
}