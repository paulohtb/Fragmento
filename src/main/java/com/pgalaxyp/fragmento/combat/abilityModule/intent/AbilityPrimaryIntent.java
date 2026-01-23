package com.pgalaxyp.fragmento.combat.abilityModule.intent;

import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import java.util.Objects;

public record AbilityPrimaryIntent(WeaponId weaponId) {
    public AbilityPrimaryIntent { Objects.requireNonNull(weaponId); }
}