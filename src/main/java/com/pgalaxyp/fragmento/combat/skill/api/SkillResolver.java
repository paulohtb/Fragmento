package com.pgalaxyp.fragmento.combat.skill.api;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import java.util.Objects;

public interface SkillResolver {
    AbilityId resolveAbility(ActorId actorId, WeaponId weaponId, ComboId comboId, int stepIndex, AbilityId baseAbility, GameState state);

    static SkillResolver none() {
        return (actorId, weaponId, comboId, stepIndex, baseAbility, state) -> {
            Objects.requireNonNull(actorId);
            Objects.requireNonNull(weaponId);
            Objects.requireNonNull(comboId);
            Objects.requireNonNull(baseAbility);
            Objects.requireNonNull(state);
            if (stepIndex < 0) throw new IllegalArgumentException();
            return baseAbility;
        };
    }
}