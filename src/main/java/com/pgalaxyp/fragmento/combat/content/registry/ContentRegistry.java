package com.pgalaxyp.fragmento.combat.content.registry;

import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.def.*;
import com.pgalaxyp.fragmento.combat.action.model.*;
import com.pgalaxyp.fragmento.combat.content.catalog.*;
import com.pgalaxyp.fragmento.combat.content.bindings.*;
import java.util.*;

public final class ContentRegistry {
    private final NavigableMap<ActionId, ActionDef> actions = new TreeMap<>();
    private final NavigableMap<WeaponId, WeaponDef> weapons = new TreeMap<>();
    private final DefaultEffectCatalog effects = new DefaultEffectCatalog();
    private final DefaultComboCatalog combos = new DefaultComboCatalog();
    private final DefaultActionCycleCatalog cycles = new DefaultActionCycleCatalog();

    public void action(ActionDef def) { actions.put(Objects.requireNonNull(def).id(), def); }
    public void weapon(WeaponDef def) { weapons.put(Objects.requireNonNull(def).id(), def); }
    public void effect(EffectDef def) { effects.register(Objects.requireNonNull(def)); }

    public DefaultComboCatalog combos() { return combos; }
    public DefaultActionCycleCatalog cycles() { return cycles; }

    public GameContent build() {
        return new GameContent(actions, weapons, effects, combos, cycles);
    }
}