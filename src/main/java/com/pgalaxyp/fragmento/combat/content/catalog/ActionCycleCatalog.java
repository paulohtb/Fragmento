package com.pgalaxyp.fragmento.combat.content.catalog;

import com.pgalaxyp.fragmento.combat.cycle.model.*;
import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public final class ActionCycleCatalog {

    public record Key(ComboId comboId, WeaponId weaponId) {}

    private final Map<Key, ActionCycleDef> defs;

    public ActionCycleCatalog(Map<Key, ActionCycleDef> defs) {
        this.defs = Map.copyOf(defs);
    }

    public Optional<ActionCycleDef> cycle(ComboId comboId, WeaponId weaponId) {
        return Optional.ofNullable(defs.get(new Key(comboId, weaponId)));
    }
}