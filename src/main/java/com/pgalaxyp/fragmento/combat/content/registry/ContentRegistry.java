package com.pgalaxyp.fragmento.combat.content.registry;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.def.*;
import com.pgalaxyp.fragmento.combat.content.catalog.*;
import com.pgalaxyp.fragmento.combat.content.GameContent;
import com.pgalaxyp.fragmento.combat.skill.api.SkillRule;
import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import com.pgalaxyp.fragmento.combat.effect.model.EffectDef;
import java.util.*;

public final class ContentRegistry {
    private final NavigableMap<WeaponId, WeaponDef> weapons = new TreeMap<>();
    private final Map<EffectId, EffectDef> effects = new TreeMap<>();
    private final Map<WeaponId, ComboCatalog.Entry> combos = new TreeMap<>();
    private final Map<ComboId, List<AbilityId>> actions = new TreeMap<>();
    private final List<SkillRule> skills = new ArrayList<>();
    private SpawnDefaults defaults;

    public void weapon(WeaponDef def) {
        if (weapons.putIfAbsent(def.id(), def) != null) throw new IllegalStateException();
    }

    public void effect(EffectDef def) {
        if (effects.putIfAbsent(def.id(), def) != null) throw new IllegalStateException();
    }

    public void comboBase(WeaponId weaponId, ComboId comboId, ComboPattern pattern) {
        if (combos.putIfAbsent(weaponId, new ComboCatalog.Entry(comboId, pattern)) != null) throw new IllegalStateException();
    }

    public void comboStepAbilities(ComboId comboId, List<AbilityId> abilitiesByStep) {
        var list = List.copyOf(abilitiesByStep);
        if (list.isEmpty()) throw new IllegalArgumentException();
        if (actions.putIfAbsent(comboId, list) != null) throw new IllegalStateException();
    }

    public void skillRule(SkillRule rule) {
        skills.add(Objects.requireNonNull(rule));
    }

    public void defaults(SpawnDefaults defaults) {
        this.defaults = defaults;
    }

    public GameContent build() {
        return new GameContent(
                weapons,
                new EffectCatalog(effects),
                new ComboCatalog(combos),
                new ActionCatalog(actions),
                new SkillCatalog(List.copyOf(skills)),
                Objects.requireNonNull(defaults)
        );
    }
}