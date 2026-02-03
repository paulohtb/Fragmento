package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.abilityModule.port.AbilityPort;
import java.util.*;

public final class AbilityModule {
    public static AbilityPort create(Map<AbilityId, AbilityDefinition> defs, List<AbilityRule> rules, Map<WeaponId, AbilityId> primaryByWeapon, AbilityTuning tuning) {
        Objects.requireNonNull(tuning);
        return new AbilityEngine(defs, rules, primaryByWeapon, tuning.comboGapFrames());
    }

    private AbilityModule() {}
}