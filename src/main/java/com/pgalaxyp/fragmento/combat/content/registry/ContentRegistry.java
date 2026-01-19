package com.pgalaxyp.fragmento.combat.content.registry;

import com.pgalaxyp.fragmento.combat.action.model.*;
import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.content.catalog.*;
import com.pgalaxyp.fragmento.combat.core.def.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.cycle.model.*;
import com.pgalaxyp.fragmento.combat.effect.model.*;
import java.util.*;

public final class ContentRegistry {
    private final NavigableMap<ActionId, ActionDef> actions = new TreeMap<>();
    private final NavigableMap<WeaponId, WeaponDef> weapons = new TreeMap<>();
    private final Map<EffectId, EffectDef> effects = new TreeMap<>();
    private final Map<WeaponId, ComboCatalog.Entry> combos = new TreeMap<>();
    private final Map<ActionCycleCatalog.Key, ActionCycleDef> cycles = new HashMap<>();
    private SpawnDefaults defaults;

    public void action(ActionDef def) {
        Objects.requireNonNull(def);
        if (actions.putIfAbsent(def.id(), def) != null) throw new IllegalStateException();
    }

    public void weapon(WeaponDef def) {
        Objects.requireNonNull(def);
        if (weapons.putIfAbsent(def.id(), def) != null) throw new IllegalStateException();
    }

    public void effect(EffectDef def) {
        Objects.requireNonNull(def);
        if (effects.putIfAbsent(def.id(), def) != null) throw new IllegalStateException();
    }

    public void comboBase(WeaponId weaponId, ComboId comboId, ComboPattern pattern) {
        Objects.requireNonNull(weaponId);
        Objects.requireNonNull(comboId);
        Objects.requireNonNull(pattern);
        if (combos.putIfAbsent(weaponId, new ComboCatalog.Entry(comboId, pattern)) != null) throw new IllegalStateException();
    }

    public void cycle(ComboId comboId, WeaponId weaponId, ActionId actionId) {
        Objects.requireNonNull(comboId);
        Objects.requireNonNull(weaponId);
        Objects.requireNonNull(actionId);
        cycles.putIfAbsent(new ActionCycleCatalog.Key(comboId, weaponId), new ActionCycleDef(comboId, actionId));
    }

    public void defaults(SpawnDefaults defaults) { this.defaults = Objects.requireNonNull(defaults); }

    public GameContent build() {
        return new GameContent(
                actions,
                weapons,
                new EffectCatalog(effects),
                new ComboCatalog(combos),
                new ActionCycleCatalog(cycles),
                Objects.requireNonNull(defaults, "defaults")
        );
    }
}