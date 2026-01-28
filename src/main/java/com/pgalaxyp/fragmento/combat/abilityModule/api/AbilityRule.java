package com.pgalaxyp.fragmento.combat.abilityModule.api;

import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import java.util.Objects;

public record AbilityRule(String ruleId, ClassId classId, WeaponId weaponId, Integer stepIndex, AbilityId baseAbility, AbilityId resultAbility) {
    public AbilityRule {
        Objects.requireNonNull(ruleId);
        Objects.requireNonNull(resultAbility);
        if (stepIndex != null && stepIndex < 0) throw new IllegalArgumentException();
        if (classId == null && weaponId == null && stepIndex == null && baseAbility == null) throw new IllegalArgumentException();
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
        if (classId == null) return true;
        var a = state.findActor(actorId).orElse(null);
        return a != null && classId.equals(a.classId());
    }
}