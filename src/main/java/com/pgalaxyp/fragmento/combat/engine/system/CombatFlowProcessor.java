package com.pgalaxyp.fragmento.combat.engine.system;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.ability.model.*;
import com.pgalaxyp.fragmento.combat.ability.system.*;
import com.pgalaxyp.fragmento.combat.actor.api.*;
import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.delta.*;
import com.pgalaxyp.fragmento.combat.intent.*;
import com.pgalaxyp.fragmento.combat.skill.api.*;
import java.util.*;

public final class CombatFlowProcessor {

    public record FlowOutput(List<StateDelta> deltas, List<AbilityInstanceView> activeAbilities) {
        public FlowOutput { deltas = List.copyOf(Objects.requireNonNull(deltas)); activeAbilities = List.copyOf(Objects.requireNonNull(activeAbilities)); }
    }

    private final GameContent content;
    private final ActorService actors;
    private final ComboService combo;
    private final SkillResolver skills;
    private final AbilityService abilities;

    public CombatFlowProcessor(GameContent content, ActorService actors, ComboService combo, SkillResolver skills, AbilityService abilities) {
        this.content = Objects.requireNonNull(content);
        this.actors = Objects.requireNonNull(actors);
        this.combo = Objects.requireNonNull(combo);
        this.skills = Objects.requireNonNull(skills);
        this.abilities = Objects.requireNonNull(abilities);
    }

    public FlowOutput process(FrameContext frame, GameState state, List<IntentEnvelope> intents) {
        if (frame == null || state == null || intents == null) throw new IllegalArgumentException();

        List<StateDelta> out = new ArrayList<>();
        out.addAll(abilities.tick(frame));

        for (IntentEnvelope env : intents) {
            ActorId actorId = env.actorId();

            if (env.intent() instanceof ActorJoinIntent) {
                out.addAll(actors.onJoin(actorId, state, content.defaults()));
                continue;
            }

            if (!(env.intent() instanceof PerformActionIntent p)) continue;

            ActorState actor = state.findActor(actorId).orElse(null);
            if (actor == null) continue;

            WeaponId weapon = actor.equippedWeaponId().orElse(null);
            if (weapon == null) continue;

            var entry = content.combos().baseFor(weapon).orElse(null);
            if (entry == null) continue;

            ComboResult r = combo.decide(actorId, entry.comboId(), entry.pattern(), p.input());
            if (!(r instanceof ComboResult.Progress pr)) continue;

            AbilityId base = content.comboAbilities().abilityForStep(entry.comboId(), pr.stepIndex()).orElse(null);
            if (base == null) continue;

            AbilityId resolved = skills.resolveAbility(actorId, weapon, entry.comboId(), pr.stepIndex(), base, state);
            out.addAll(abilities.tryStart(resolved, actorId, frame, state));
        }

        return new FlowOutput(List.copyOf(out), abilities.activeViews(frame.frameId()));
    }
}
