package com.pgalaxyp.fragmento.combat.content.catalog;

import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public final class DefaultComboCatalog implements ComboCatalog {

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