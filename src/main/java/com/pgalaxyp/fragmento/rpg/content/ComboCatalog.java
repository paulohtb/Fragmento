package com.pgalaxyp.fragmento.rpg.content;

import com.pgalaxyp.fragmento.rpg.combo.model.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;

public interface ComboCatalog {
    Optional<Entry> baseFor(WeaponId weaponId);

    record Entry(ComboId comboId, ComboPattern pattern) {
        public Entry { if (comboId == null || pattern == null) throw new IllegalArgumentException(); }
    }
}