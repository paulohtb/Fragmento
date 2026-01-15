package com.pgalaxyp.fragmento.combat.content.catalog;

import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public interface ComboCatalog {

    Optional<Entry> baseFor(WeaponId weaponId);

    record Entry(ComboId comboId, ComboPattern pattern) {
        public Entry {
            if (comboId == null || pattern == null) { throw new IllegalArgumentException(); }
        }
    }
}