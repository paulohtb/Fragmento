package com.pgalaxyp.fragmento.rpg.core.rule;

import java.util.ArrayList;
import java.util.List;
import com.pgalaxyp.fragmento.rpg.core.domain.WeaponByAction;
import com.pgalaxyp.fragmento.rpg.core.domain.WeaponDef;
import com.pgalaxyp.fragmento.rpg.core.domain.event.ComboCompleted;
import com.pgalaxyp.fragmento.rpg.core.domain.event.ComboStepEmitted;
import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.domain.event.VisualEffectRequested;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.EffectId;
import com.pgalaxyp.fragmento.rpg.core.domain.intent.ComboAdvanceIntent;
import com.pgalaxyp.fragmento.rpg.core.domain.intent.DomainIntent;
import com.pgalaxyp.fragmento.rpg.core.spec.ComboSpec;
import com.pgalaxyp.fragmento.rpg.core.spec.GameSpec;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.ComboState;
import com.pgalaxyp.fragmento.rpg.core.state.delta.ComboAdvanced;
import com.pgalaxyp.fragmento.rpg.core.state.delta.ComboEnded;
import com.pgalaxyp.fragmento.rpg.core.state.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.snapshot.GameSnapshot;

public final class ComboRule implements FrameRule {

    @Override
    public RuleResult apply(
            GameSpec spec,
            GameSnapshot snapshot,
            List<DomainIntent> intents,
            WeaponByAction weapons
    ) {
        List<StateDelta> deltas = new ArrayList<>();
        List<DomainEvent> events = new ArrayList<>();

        ComboSpec comboSpec = spec.combo();

        for (DomainIntent di : intents) {
            if (!(di instanceof ComboAdvanceIntent intent)) {
                continue;
            }

            ActorId actorId = intent.actorId();
            ActorState actor = snapshot.actors().get(actorId);
            if (actor == null) {
                continue;
            }

            ComboState combo = actor.combo();
            if (combo == null) {
                continue;
            }

            WeaponDef weapon = weapons.byAction().get(intent.actionId());
            if (weapon == null) {
                continue;
            }

            int rawStepsTotal = weapon.action().combo().stepsTotal();
            int stepsTotal = clamp(rawStepsTotal, comboSpec.minStepsTotal(), comboSpec.maxStepsTotal());

            int next = combo.stepIndex() + 1;

            if (next >= stepsTotal) {
                deltas.add(new ComboEnded(actorId));
                events.add(new ComboCompleted(actorId, weapon.id()));
                continue;
            }

            List<EffectId> effects = weapon.action().combo().effectsPerStep();
            if (effects == null || effects.isEmpty()) {
                deltas.add(new ComboEnded(actorId));
                events.add(new ComboCompleted(actorId, weapon.id()));
                continue;
            }

            EffectId effectId = effects.get(next % effects.size());

            deltas.add(new ComboAdvanced(actorId, next));
            events.add(new ComboStepEmitted(actorId, weapon.id(), next, effectId));
            events.add(new VisualEffectRequested(actorId, effectId, next));
        }

        return new RuleResult(List.copyOf(deltas), List.copyOf(events));
    }

    private static int clamp(int v, int min, int max) {
        if (v < min) return min;
        if (v > max) return max;
        return v;
    }
}