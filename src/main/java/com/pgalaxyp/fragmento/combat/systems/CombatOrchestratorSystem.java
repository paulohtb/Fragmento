package com.pgalaxyp.fragmento.combat.systems;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import com.pgalaxyp.fragmento.combat.content.GameContent;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.events.ability.AbilityRequested;
import com.pgalaxyp.fragmento.combat.events.ability.AbilityStarted;
import com.pgalaxyp.fragmento.combat.events.combo.*;
import com.pgalaxyp.fragmento.combat.events.effect.EffectTriggered;
import com.pgalaxyp.fragmento.combat.flow.FrameBus;
import com.pgalaxyp.fragmento.combat.flow.FrameSystem;
import com.pgalaxyp.fragmento.combat.skill.api.SkillResolver;
import java.util.Objects;

public final class CombatOrchestratorSystem implements FrameSystem {

    private final GameContent content;
    private final SkillResolver skills;
    private final boolean plan;
    private final boolean react;

    private CombatOrchestratorSystem(GameContent content, SkillResolver skills, boolean plan, boolean react) {
        if (!plan && !react) throw new IllegalArgumentException();
        if (plan && (content == null || skills == null)) throw new IllegalArgumentException();
        this.content = content;
        this.skills = skills;
        this.plan = plan;
        this.react = react;
    }

    public static CombatOrchestratorSystem plan(GameContent content, SkillResolver skills) {
        return new CombatOrchestratorSystem(Objects.requireNonNull(content), Objects.requireNonNull(skills), true, false);
    }

    public static CombatOrchestratorSystem react() {
        return new CombatOrchestratorSystem(null, null, false, true);
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        if (plan) {
            for (ComboStarted e : bus.events(ComboStarted.class)) request(state, bus, e.actorId(), e.snapshot().comboId(), e.snapshot().stepIndex());
            for (ComboAdvanced e : bus.events(ComboAdvanced.class)) request(state, bus, e.actorId(), e.snapshot().comboId(), e.snapshot().stepIndex());
            for (ComboCompleted e : bus.events(ComboCompleted.class)) request(state, bus, e.actorId(), e.snapshot().comboId(), e.snapshot().stepIndex());
        }
        if (react) for (AbilityStarted e : bus.events(AbilityStarted.class)) bus.publish(new EffectTriggered(e.startEffect(), e.source(), e.target()));
    }

    private void request(GameState state, FrameBus bus, ActorId actorId, com.pgalaxyp.fragmento.combat.combo.api.ComboId comboId, int stepIndex) {
        var actor = state.findActor(actorId).orElse(null);
        if (actor == null) return;

        WeaponId weapon = actor.equippedWeaponId().orElse(null);
        if (weapon == null) return;

        AbilityId base = content.actions().abilityForStep(comboId, stepIndex).orElse(null);
        if (base == null) return;

        bus.publish(new AbilityRequested(actorId, skills.resolveAbility(actorId, weapon, comboId, stepIndex, base, state)));
    }
}
