package com.pgalaxyp.fragmento.combat.abilityModule.api;

import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import java.util.Objects;

public record AbilityRule(String ruleId, ClassId classId, WeaponId weaponId, Integer stepIndex, AbilityId baseAbility, AbilityId resultAbility) {
    public AbilityRule {
        Objects.requireNonNull(ruleId);
        Objects.requireNonNull(resultAbility);
        if (stepIndex != null && stepIndex < 0) throw new IllegalArgumentException();
        if (classId == null && weaponId == null && stepIndex == null && baseAbility == null) throw new IllegalArgumentException();
    }
}