package com.pgalaxyp.fragmento.combat.content;

import com.pgalaxyp.fragmento.combat.content.catalog.*;
import com.pgalaxyp.fragmento.combat.core.def.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public record GameContent(
        NavigableMap<WeaponId, WeaponDef> weapons,
        EffectCatalog effects,
        ComboCatalog combos,
        ComboAbilityCatalog comboAbilities,
        SpawnDefaults defaults
) {
    public GameContent {
        weapons = Collections.unmodifiableNavigableMap(new TreeMap<>(Objects.requireNonNull(weapons)));
        effects = Objects.requireNonNull(effects);
        combos = Objects.requireNonNull(combos);
        comboAbilities = Objects.requireNonNull(comboAbilities);
        defaults = Objects.requireNonNull(defaults);
    }

    public Optional<WeaponDef> weapon(WeaponId id) { return Optional.ofNullable(weapons.get(id)); }
}
