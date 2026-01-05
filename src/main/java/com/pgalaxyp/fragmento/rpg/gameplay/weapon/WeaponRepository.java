package com.pgalaxyp.fragmento.rpg.gameplay.weapon;

import java.util.HashMap;
import java.util.Map;

public final class WeaponRepository {

    private final Map<Long, String> equipped = new HashMap<>();

    public void equip(long actorId, String weaponId) {
        equipped.put(actorId, weaponId);
    }

    public String equippedWeapon(long actorId) {
        return equipped.get(actorId);
    }
}