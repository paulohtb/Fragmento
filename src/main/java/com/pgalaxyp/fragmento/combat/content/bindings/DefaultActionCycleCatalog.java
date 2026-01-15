package com.pgalaxyp.fragmento.combat.content.bindings;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.cycle.model.*;
import java.util.*;

public final class DefaultActionCycleCatalog implements ActionCycleCatalog {
    private final NavigableMap<WeaponId, ActionCycleDef> byWeapon = new TreeMap<>();

    public void register(WeaponId weaponId, ActionCycleDef def) {
        Objects.requireNonNull(weaponId);
        Objects.requireNonNull(def);
        if (byWeapon.putIfAbsent(weaponId, def) != null) throw new IllegalStateException();
    }

    @Override
    public Optional<ActionCycleDef> cycleFor(WeaponId weaponId) {
        Objects.requireNonNull(weaponId);
        return Optional.ofNullable(byWeapon.get(weaponId));
    }
}