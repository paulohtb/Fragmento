package com.pgalaxyp.fragmento.combat.skill.system;

import com.pgalaxyp.fragmento.combat.actor.api.ActorId;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.skill.api.*;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import java.util.*;

public final class DefaultSkillResolver implements SkillResolver {
    private final List<SkillRule> rules;

    private DefaultSkillResolver(List<SkillRule> rules) {
        Objects.requireNonNull(rules);
        var copy = new ArrayList<SkillRule>(rules.size());
        for (SkillRule r : rules) {
            copy.add(Objects.requireNonNull(r));
        }
        this.rules = List.copyOf(copy);
    }

    public static DefaultSkillResolver of(List<SkillRule> rules) {
        return new DefaultSkillResolver(rules);
    }

    @Override
    public AbilityId resolveAbility(ActorId actorId, WeaponId weaponId, int stepIndex, AbilityId baseAbility, GameState state) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(weaponId);
        Objects.requireNonNull(baseAbility);
        Objects.requireNonNull(state);
        if (stepIndex < 0) throw new IllegalArgumentException();

        for (SkillRule r : rules) {
            if (r.matches(actorId, weaponId, stepIndex, baseAbility, state)) {
                return r.resultAbility();
            }
        }
        return baseAbility;
    }
}