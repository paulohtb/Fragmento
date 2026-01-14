package com.pgalaxyp.fragmento.rpg.combo.registry;

import com.pgalaxyp.fragmento.rpg.combo.model.*;
import com.pgalaxyp.fragmento.rpg.content.ComboCatalog;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;

public final class InMemoryComboCatalog implements ComboCatalog {

    private final NavigableMap<WeaponId, Entry> byWeapon = new TreeMap<>();

    public void register(WeaponId weaponId, ComboId comboId, ComboPattern pattern) {
        if (weaponId == null || comboId == null || pattern == null) throw new IllegalArgumentException();
        if (byWeapon.containsKey(weaponId)) throw new IllegalStateException();
        byWeapon.put(weaponId, new Entry(comboId, pattern));
    }

    @Override
    public Optional<Entry> baseFor(WeaponId weaponId) {
        if (weaponId == null) throw new IllegalArgumentException();
        return Optional.ofNullable(byWeapon.get(weaponId));
    }
}