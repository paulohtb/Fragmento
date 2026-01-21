package com.pgalaxyp.fragmento.combat.skill.api;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;
import com.pgalaxyp.fragmento.combat.core.state.GameState;

public interface SkillResolver {
    AbilityId resolveAbility(ActorId actorId, WeaponId weaponId, int stepIndex, AbilityId baseAbility, GameState state);
}