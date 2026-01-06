package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.gameplay.weapon.WeaponDef;

public final class WeaponBootstrap {

    private WeaponBootstrap() {}

    public static void register(WeaponDef flute, com.pgalaxyp.fragmento.rpg.gameplay.state.WeaponRepository weapons) {
        weapons.registerDef(flute);
    }
}