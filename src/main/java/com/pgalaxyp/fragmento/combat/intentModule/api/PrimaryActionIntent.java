package com.pgalaxyp.fragmento.combat.intentModule.api;

import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import java.util.Objects;

public record PrimaryActionIntent(WeaponId weaponId) {
    public PrimaryActionIntent { Objects.requireNonNull(weaponId); }
}