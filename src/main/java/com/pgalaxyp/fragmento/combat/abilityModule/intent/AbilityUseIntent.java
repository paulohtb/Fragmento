package com.pgalaxyp.fragmento.combat.abilityModule.intent;

import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityId;
import java.util.Objects;

public record AbilityUseIntent(AbilityId abilityId, WeaponId weaponId) {
    public AbilityUseIntent {
        Objects.requireNonNull(abilityId);
        Objects.requireNonNull(weaponId);
    }
}