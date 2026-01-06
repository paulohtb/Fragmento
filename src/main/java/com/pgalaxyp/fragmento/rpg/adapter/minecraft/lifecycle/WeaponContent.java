package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.gameplay.weapon.WeaponDef;

public final class WeaponContent {

    private WeaponContent() {}

    public static WeaponDef flute() {
        return new WeaponDef("FLUTE", 0.5);
    }
}