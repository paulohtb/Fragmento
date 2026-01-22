package com.pgalaxyp.fragmento.combat.skill.system;

import com.pgalaxyp.fragmento.combat.ability.event.AbilityRequested;
import com.pgalaxyp.fragmento.combat.ability.event.AbilityResolved;
import com.pgalaxyp.fragmento.combat.flow.FrameBus;
import com.pgalaxyp.fragmento.combat.flow.FrameSystem;
import com.pgalaxyp.fragmento.combat.skill.api.SkillResolver;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.combo.system.ComboService;
import java.util.Objects;

public final class SkillResolution implements FrameSystem {
    private final SkillResolver resolver;
    private final ComboService combos;

    public SkillResolution(SkillResolver resolver, ComboService combos) {
        this.resolver = Objects.requireNonNull(resolver);
        this.combos = Objects.requireNonNull(combos);
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        for (AbilityRequested requested : bus.events(AbilityRequested.class)) {
            if (state.findActor(requested.actorId()).isEmpty()) continue;

            int step = combos.resolveStep(requested.actorId(), requested.weaponId(), frame.frameId());
            var resolved = resolver.resolveAbility(requested.actorId(), requested.weaponId(), step, requested.abilityId(), state);
            bus.publish(new AbilityResolved(requested.actorId(), resolved));
        }
    }
}