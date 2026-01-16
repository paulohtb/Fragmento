package com.pgalaxyp.fragmento.combat.content.catalog;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public final class WeaponCatalog {

    private final Set<WeaponId> weapons;

    public WeaponCatalog(Set<WeaponId> weapons) {
        this.weapons = Set.copyOf(weapons);
    }

    public boolean exists(WeaponId id) {
        return weapons.contains(id);
    }
}