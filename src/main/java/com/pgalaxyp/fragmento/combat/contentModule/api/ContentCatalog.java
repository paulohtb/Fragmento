package com.pgalaxyp.fragmento.combat.contentModule.api;

import com.pgalaxyp.fragmento.combat.effectModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import java.util.*;

public record ContentCatalog(Map<AbilityId, AbilityDefinition> abilities, Map<EffectId, EffectDef> effects, List<AbilityRule> abilityRules, Map<WeaponId, AbilityId> primaryBindings, Map<AbilityId, AbilityEffectSpec> abilityEffects) {
    public ContentCatalog {
        abilities = Map.copyOf(Objects.requireNonNull(abilities));
        effects = Map.copyOf(Objects.requireNonNull(effects));
        abilityRules = List.copyOf(Objects.requireNonNull(abilityRules));
        primaryBindings = Map.copyOf(Objects.requireNonNull(primaryBindings));
        abilityEffects = Map.copyOf(Objects.requireNonNull(abilityEffects));
    }

    public static ContentCatalog of(List<? extends ContentPack> packs) {
        Objects.requireNonNull(packs);
        var abilities = new LinkedHashMap<AbilityId, AbilityDefinition>();
        var effects = new LinkedHashMap<EffectId, EffectDef>();
        var rules = new ArrayList<AbilityRule>();
        var primary = new LinkedHashMap<WeaponId, AbilityId>();
        var abilityEffects = new LinkedHashMap<AbilityId, AbilityEffectSpec>();
        for (ContentPack p : packs) {
            if (p == null) throw new IllegalArgumentException();
            for (AbilityDefinition d : p.abilities()) putUnique(abilities, d.id(), d, "abilityId");
            for (EffectDef d : p.effects()) putUnique(effects, d.id(), d, "effectId");
            p.abilityRules().forEach(r -> rules.add(Objects.requireNonNull(r)));
            for (var e : p.primaryBindings().entrySet()) {
                if (e.getKey() == null || e.getValue() == null) throw new IllegalArgumentException();
                putUnique(primary, e.getKey(), e.getValue(), "weaponId");
            }
            for (var e : p.abilityEffects().entrySet()) {
                if (e.getKey() == null || e.getValue() == null) throw new IllegalArgumentException();
                putUnique(abilityEffects, e.getKey(), e.getValue(), "abilityId(effect)");
            }
        }

        return new ContentCatalog(abilities, effects, rules, primary, abilityEffects);
    }

    private static <K, V> void putUnique(Map<K, V> map, K key, V value, String label) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        if (map.putIfAbsent(key, value) != null) throw new IllegalStateException("Duplicate " + label + "=" + key);
    }
}