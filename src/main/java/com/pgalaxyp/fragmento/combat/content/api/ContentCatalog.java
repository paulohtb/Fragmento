package com.pgalaxyp.fragmento.combat.content.api;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;
import com.pgalaxyp.fragmento.combat.effect.api.*;
import com.pgalaxyp.fragmento.combat.skill.api.SkillRule;
import java.util.*;

public record ContentCatalog(Map<AbilityId, AbilityDef> abilities, Map<EffectId, EffectDef> effects, List<SkillRule> skills, Map<WeaponId, AbilityId> primaryBindings) {
    public ContentCatalog {
        abilities = Map.copyOf(Objects.requireNonNull(abilities));
        effects = Map.copyOf(Objects.requireNonNull(effects));
        skills = List.copyOf(Objects.requireNonNull(skills));
        primaryBindings = Map.copyOf(Objects.requireNonNull(primaryBindings));
    }

    public static ContentCatalog of(List<? extends ContentPack> packs) {
        Objects.requireNonNull(packs);

        var abilities = new LinkedHashMap<AbilityId, AbilityDef>();
        var effects = new LinkedHashMap<EffectId, EffectDef>();
        var skills = new ArrayList<SkillRule>();
        var primary = new LinkedHashMap<WeaponId, AbilityId>();

        for (ContentPack p : packs) {
            if (p == null) throw new IllegalArgumentException();

            for (AbilityDef d : p.abilities()) putUnique(abilities, d.id(), d, "abilityId");
            for (EffectDef d : p.effects()) putUnique(effects, d.id(), d, "effectId");

            p.skills().forEach(r -> skills.add(Objects.requireNonNull(r)));

            for (var e : p.primaryBindings().entrySet()) {
                if (e.getKey() == null || e.getValue() == null) throw new IllegalArgumentException();
                putUnique(primary, e.getKey(), e.getValue(), "weaponId");
            }
        }

        return new ContentCatalog(abilities, effects, skills, primary);
    }

    private static <K, V> void putUnique(Map<K, V> map, K key, V value, String label) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        if (map.putIfAbsent(key, value) != null) throw new IllegalStateException("Duplicate " + label + "=" + key);
    }
}