package com.pgalaxyp.fragmento.combat.content;

import com.pgalaxyp.fragmento.combat.action.model.*;
import com.pgalaxyp.fragmento.combat.content.bindings.*;
import com.pgalaxyp.fragmento.combat.content.catalog.*;
import com.pgalaxyp.fragmento.combat.core.def.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public record GameContent(
        NavigableMap<ActionId, ActionDef> actions,
        NavigableMap<WeaponId, WeaponDef> weapons,
        EffectCatalog effects,
        ComboCatalog combos,
        ActionCycleCatalog cycles,
        SpawnDefaults defaults
) {
    public GameContent {
        actions = Collections.unmodifiableNavigableMap(new TreeMap<>(Objects.requireNonNull(actions)));
        weapons = Collections.unmodifiableNavigableMap(new TreeMap<>(Objects.requireNonNull(weapons)));
        effects = Objects.requireNonNull(effects);
        combos = Objects.requireNonNull(combos);
        cycles = Objects.requireNonNull(cycles);
        defaults = Objects.requireNonNull(defaults);
    }

    public Optional<ActionDef> action(ActionId id) { return Optional.ofNullable(actions.get(id)); }
    public Optional<WeaponDef> weapon(WeaponId id) { return Optional.ofNullable(weapons.get(id)); }
}