package com.pgalaxyp.fragmento.combat.skill.api;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import java.util.*;

public interface SkillResolver {
    AbilityId resolveAbility(
            ActorId actorId,
            WeaponId weaponId,
            ComboId comboId,
            int stepIndex,
            AbilityId baseAbility,
            GameState state
    );

    static SkillResolver none() {
        return (a, w, c, i, b, s) -> Objects.requireNonNull(b);
    }
}
