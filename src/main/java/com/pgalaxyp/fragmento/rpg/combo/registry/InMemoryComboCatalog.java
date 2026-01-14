package com.pgalaxyp.fragmento.rpg.combo.registry;

import com.pgalaxyp.fragmento.rpg.combo.model.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;

public final class InMemoryComboCatalog implements ComboCatalog {

    private final NavigableMap<WeaponId, ComboDefinition> byWeapon = new TreeMap<>();

    public void register(WeaponId weaponId, ComboDefinition def) {
        Objects.requireNonNull(weaponId);
        Objects.requireNonNull(def);

        if (byWeapon.containsKey(weaponId)) {
            throw new IllegalStateException();
        }
        byWeapon.put(weaponId, def);
    }

    @Override
    public Optional<ComboDefinition> baseFor(WeaponId weaponId) {
        Objects.requireNonNull(weaponId);
        return Optional.ofNullable(byWeapon.get(weaponId));
    }
}