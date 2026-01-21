package com.pgalaxyp.fragmento.combat.orchestrator;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import com.pgalaxyp.fragmento.combat.content.GameContent;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.events.ability.AbilityRequested;
import com.pgalaxyp.fragmento.combat.events.ability.AbilityStarted;
import com.pgalaxyp.fragmento.combat.events.combo.ComboAdvanced;
import com.pgalaxyp.fragmento.combat.events.combo.ComboCompleted;
import com.pgalaxyp.fragmento.combat.events.combo.ComboStarted;
import com.pgalaxyp.fragmento.combat.events.effect.EffectTriggered;
import com.pgalaxyp.fragmento.combat.flow.FrameBus;
import com.pgalaxyp.fragmento.combat.skill.api.SkillResolver;
import java.util.Objects;

public final class DefaultCombatOrchestrator implements CombatOrchestrator {

    private final GameContent content;
    private final SkillResolver skills;

    public DefaultCombatOrchestrator(GameContent content, SkillResolver skills) {
        this.content = Objects.requireNonNull(content);
        this.skills = Objects.requireNonNull(skills);
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        plan(state, bus);
        react(bus);
    }

    private void plan(GameState state, FrameBus bus) {
        for (ComboStarted e : bus.events(ComboStarted.class)) {
            request(state, bus, e.actorId(), e.snapshot().comboId(), e.snapshot().stepIndex());
        }
        for (ComboAdvanced e : bus.events(ComboAdvanced.class)) {
            request(state, bus, e.actorId(), e.snapshot().comboId(), e.snapshot().stepIndex());
        }
        for (ComboCompleted e : bus.events(ComboCompleted.class)) {
            request(state, bus, e.actorId(), e.snapshot().comboId(), e.snapshot().stepIndex());
        }
    }

    private void react(FrameBus bus) {
        for (AbilityStarted e : bus.events(AbilityStarted.class)) {
            bus.publish(new EffectTriggered(e.startEffect(), e.source(), e.target()));
        }
    }

    private void request(GameState state, FrameBus bus, ActorId actorId, com.pgalaxyp.fragmento.combat.combo.api.ComboId comboId, int stepIndex) {
        var actor = state.findActor(actorId).orElse(null);
        if (actor == null) return;

        WeaponId weapon = actor.equippedWeaponId().orElse(null);
        if (weapon == null) return;

        AbilityId base = content.actions().abilityForStep(comboId, stepIndex).orElse(null);
        if (base == null) return;

        AbilityId resolved = skills.resolveAbility(actorId, weapon, comboId, stepIndex, base, state);
        bus.publish(new AbilityRequested(actorId, resolved));
    }
}