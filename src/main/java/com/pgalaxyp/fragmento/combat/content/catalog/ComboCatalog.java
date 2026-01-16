package com.pgalaxyp.fragmento.combat.content.catalog;

import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public final class ComboCatalog {

    public record Entry(ComboId comboId, ComboPattern pattern) {}

    private final Map<WeaponId, Entry> base;

    public ComboCatalog(Map<WeaponId, Entry> base) {
        this.base = Map.copyOf(base);
    }

    public Optional<Entry> baseFor(WeaponId weaponId) {
        return Optional.ofNullable(base.get(weaponId));
    }
}