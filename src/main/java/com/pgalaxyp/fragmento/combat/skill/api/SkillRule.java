package com.pgalaxyp.fragmento.combat.skill.api;

import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.actor.ActorState;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import java.util.Objects;

public record SkillRule(SkillId skillId, ClassId classId, WeaponId weaponId, Integer stepIndex, AbilityId baseAbility, AbilityId resultAbility) {
    public SkillRule {
        Objects.requireNonNull(skillId);
        Objects.requireNonNull(resultAbility);
        if (stepIndex != null && stepIndex < 0) throw new IllegalArgumentException();
        boolean hasAnyFilter = classId != null || weaponId != null || stepIndex != null || baseAbility != null;
        if (!hasAnyFilter) throw new IllegalArgumentException();
    }

    public boolean matches(ActorId actorId, WeaponId weaponIdIn, int stepIndexIn, AbilityId baseAbilityIn, GameState state) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(weaponIdIn);
        Objects.requireNonNull(baseAbilityIn);
        Objects.requireNonNull(state);
        if (stepIndexIn < 0) throw new IllegalArgumentException();
        if (weaponId != null && !weaponId.equals(weaponIdIn)) return false;
        if (stepIndex != null && stepIndex != stepIndexIn) return false;
        if (baseAbility != null && !baseAbility.equals(baseAbilityIn)) return false;
        if (classId != null) {
            ActorState a = state.findActor(actorId).orElse(null);
            if (a == null) return false;
            return classId.equals(a.classId());
        }

        return true;
    }
}