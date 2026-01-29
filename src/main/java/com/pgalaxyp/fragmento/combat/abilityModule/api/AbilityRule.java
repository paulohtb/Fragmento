package com.pgalaxyp.fragmento.combat.abilityModule.api;

import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import java.util.*;

public record AbilityRule(String ruleId, ClassId classId, WeaponId weaponId, Integer stepIndex, AbilityId baseAbility, AbilityId resultAbility) {
    public AbilityRule {
        Objects.requireNonNull(ruleId);
        Objects.requireNonNull(resultAbility);
        if (stepIndex != null && stepIndex < 0) throw new IllegalArgumentException();
        if (classId == null && weaponId == null && stepIndex == null && baseAbility == null) throw new IllegalArgumentException();
    }

    public boolean matches(WeaponId weaponIdIn, int stepIndexIn, AbilityId baseAbilityIn, Optional<ClassId> actorClass) {
        Objects.requireNonNull(weaponIdIn);
        Objects.requireNonNull(baseAbilityIn);
        Objects.requireNonNull(actorClass);
        if (stepIndexIn < 0) throw new IllegalArgumentException();
        if (weaponId != null && !weaponId.equals(weaponIdIn)) return false;
        if (stepIndex != null && stepIndex != stepIndexIn) return false;
        if (baseAbility != null && !baseAbility.equals(baseAbilityIn)) return false;

        return classId == null || actorClass.filter(classId::equals).isPresent();
    }
}