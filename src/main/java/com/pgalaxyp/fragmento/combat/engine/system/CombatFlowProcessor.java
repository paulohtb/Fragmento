package com.pgalaxyp.fragmento.combat.engine.system;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import com.pgalaxyp.fragmento.combat.ability.model.AbilityInstance;
import com.pgalaxyp.fragmento.combat.ability.model.AbilityInstanceView;
import com.pgalaxyp.fragmento.combat.ability.system.AbilityService;
import com.pgalaxyp.fragmento.combat.actor.api.ActorService;
import com.pgalaxyp.fragmento.combat.combo.api.ComboDecision;
import com.pgalaxyp.fragmento.combat.combo.api.ComboService;
import com.pgalaxyp.fragmento.combat.content.GameContent;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;
import com.pgalaxyp.fragmento.combat.core.state.ActorState;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.delta.StateDelta;
import com.pgalaxyp.fragmento.combat.intent.ActorJoinIntent;
import com.pgalaxyp.fragmento.combat.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.combat.intent.PerformActionIntent;
import com.pgalaxyp.fragmento.combat.skill.api.SkillResolver;
import com.pgalaxyp.fragmento.combat.transport.snapshot.api.ActorExecutionPhase;
import com.pgalaxyp.fragmento.combat.transport.snapshot.api.ActorExecutionState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

public final class CombatFlowProcessor {

    public record FlowOutput(
            List<StateDelta> deltas,
            List<AbilityInstanceView> activeAbilities,
            NavigableMap<ActorId, ActorExecutionState> execution
    ) {
        public FlowOutput {
            deltas = List.copyOf(Objects.requireNonNull(deltas));
            activeAbilities = List.copyOf(Objects.requireNonNull(activeAbilities));
            execution = Collections.unmodifiableNavigableMap(new TreeMap<>(Objects.requireNonNull(execution)));
        }
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

        long frameId = frame.frameId();

        AbilityService.TickResult tick = abilities.tick(frame);
        List<AbilityInstance> ended = tick.ended();

        List<StateDelta> out = new ArrayList<>();
        Map<ActorId, AbilityInstance> startedByActor = new HashMap<>();

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

            ComboDecision d = combo.decide(actorId, entry.comboId(), entry.pattern(), p.input());

            boolean locked = abilities.isLocked(actorId, frameId);

            if (d instanceof ComboDecision.Reset r) {
                if (!locked) r.commit().run();
                continue;
            }

            if (!(d instanceof ComboDecision.Proposed pr)) continue;

            AbilityId base = content.comboAbilities().abilityForStep(entry.comboId(), pr.stepIndex()).orElse(null);
            if (base == null) continue;

            AbilityId resolved = skills.resolveAbility(actorId, weapon, entry.comboId(), pr.stepIndex(), base, state);

            AbilityService.StartResult sr = abilities.tryStart(resolved, actorId, frame, state);
            if (!sr.started()) continue;

            pr.commit().run();
            startedByActor.put(actorId, sr.instance());
            out.addAll(sr.deltas());
        }

        List<AbilityInstanceView> activeViews = abilities.activeViews(frameId);

        NavigableMap<ActorId, ActorExecutionState> exec = new TreeMap<>();
        Map<ActorId, AbilityInstanceView> activeByActor = new HashMap<>();
        for (AbilityInstanceView v : activeViews) activeByActor.put(v.actorId(), v);

        Map<ActorId, AbilityInstance> endedByActor = new HashMap<>();
        for (AbilityInstance e : ended) endedByActor.put(e.actorId(), e);

        for (ActorId actorId : state.actors().keySet()) {
            AbilityInstance endedInst = endedByActor.get(actorId);
            if (endedInst != null) {
                exec.put(actorId, new ActorExecutionState(ActorExecutionPhase.ABILITY_END, AbilityInstanceView.of(endedInst)));
                continue;
            }

            AbilityInstance started = startedByActor.get(actorId);
            if (started != null) {
                exec.put(actorId, new ActorExecutionState(ActorExecutionPhase.ABILITY_START, AbilityInstanceView.of(started)));
                continue;
            }

            AbilityInstanceView active = activeByActor.get(actorId);
            if (active != null) {
                exec.put(actorId, new ActorExecutionState(ActorExecutionPhase.ABILITY_ACTIVE, active));
                continue;
            }

            exec.put(actorId, new ActorExecutionState(ActorExecutionPhase.IDLE, null));
        }

        return new FlowOutput(List.copyOf(out), activeViews, exec);
    }
}