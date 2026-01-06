package com.pgalaxyp.fragmento.rpg.core.content.weapon;

import com.pgalaxyp.fragmento.rpg.core.content.combo.FluteComboSequence;
import com.pgalaxyp.fragmento.rpg.core.domain.weapon.WeaponDef;

public final class FluteWeapon {

    public static WeaponDef create() {
        return new WeaponDef(
                "FLUTE",
                FluteComboSequence.create(),
                0.5
        );
    }
}