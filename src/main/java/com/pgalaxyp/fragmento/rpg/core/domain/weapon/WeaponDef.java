package com.pgalaxyp.fragmento.rpg.core.domain.weapon;

import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboSequence;

public record WeaponDef(
        String weaponId,
        ComboSequence combo,
        double baseCooldownSeconds
) {}